package org.fatsnake.fatrpc.framework.core.client;

import com.alibaba.fastjson.JSON;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.fatsnake.fatrpc.framework.core.common.RpcDecoder;
import org.fatsnake.fatrpc.framework.core.common.RpcEncoder;
import org.fatsnake.fatrpc.framework.core.common.RpcInvocation;
import org.fatsnake.fatrpc.framework.core.common.RpcProtocol;
import org.fatsnake.fatrpc.framework.core.common.config.ClientConfig;
import org.fatsnake.fatrpc.framework.core.common.config.PropertiesBootstrap;
import org.fatsnake.fatrpc.framework.core.common.event.IRpcListenerLoader;
import org.fatsnake.fatrpc.framework.core.common.utils.CommonUtils;
import org.fatsnake.fatrpc.framework.core.proxy.javassist.JavassistProxyFactory;
import org.fatsnake.fatrpc.framework.core.proxy.jdk.JDKProxyFactory;
import org.fatsnake.fatrpc.framework.core.registy.URL;
import org.fatsnake.fatrpc.framework.core.registy.zookeeper.AbstractRegister;
import org.fatsnake.fatrpc.framework.core.registy.zookeeper.ZookeeperRegister;
import org.fatsnake.fatrpc.framework.core.router.RandomRouterImpl;
import org.fatsnake.fatrpc.framework.core.router.RotateRouterImpl;
import org.fatsnake.fatrpc.framework.core.server.DataService;
import org.fatsnake.fatrpc.framework.interfaces.IDataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static org.fatsnake.fatrpc.framework.core.common.cache.CommonClientCache.IROUTER;
import static org.fatsnake.fatrpc.framework.core.common.cache.CommonClientCache.SEND_QUEUE;
import static org.fatsnake.fatrpc.framework.core.common.cache.CommonClientCache.SUBSCRIBE_SERVICE_LIST;
import static org.fatsnake.fatrpc.framework.core.common.constans.RpcConstants.JAVASSIST_PROXY_TYPE;
import static org.fatsnake.fatrpc.framework.core.common.constans.RpcConstants.RANDOM_ROUTER_TYPE;
import static org.fatsnake.fatrpc.framework.core.common.constans.RpcConstants.ROTATE_ROUTER_TYPE;


/**
 * @Author fatsnake
 * @Date created in 8:22 上午 2021/11/29
 * <p>
 * 核心思想：将请求发送任务交给单独的IO线程区负责，实现异步化，提升发送性能。
 * <p>
 * 客户端首先需要通过一个代理工厂获取被调用对象的代理对象，然后通过代理对象
 * 将数据放入发送队列，最后会有一个异步线程将发送队列内部的数据一个个地发送
 * 给到服务端，并且等待服务端响应对应的数据结果。
 */
public class Client {

    private Logger logger = LoggerFactory.getLogger(Client.class);

    public static EventLoopGroup clientGroup = new NioEventLoopGroup();

    private ClientConfig clientConfig;

    private AbstractRegister abstractRegister;

    private IRpcListenerLoader iRpcListenerLoader;

    private Bootstrap bootstrap = new Bootstrap();

    public Bootstrap getBootstrap() {
        return bootstrap;
    }

    public ClientConfig getClientConfig() {
        return clientConfig;
    }

    public void setClientConfig(ClientConfig clientConfig) {
        this.clientConfig = clientConfig;
    }

    public RpcReference initClientApplication() {
        EventLoopGroup clientGroup = new NioEventLoopGroup();
        bootstrap.group(clientGroup);
        bootstrap.channel(NioSocketChannel.class);
        bootstrap.handler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) throws Exception {
                ch.pipeline().addLast(new RpcEncoder());
                ch.pipeline().addLast(new RpcDecoder());
                ch.pipeline().addLast(new ClientHandler());
            }
        });
        iRpcListenerLoader = new IRpcListenerLoader();
        iRpcListenerLoader.init();
        // 初始化客户端应用信息
        this.clientConfig = PropertiesBootstrap.loadClientConfigFromLocal();
        RpcReference rpcReference;
        if (JAVASSIST_PROXY_TYPE.equals(clientConfig.getProxyType())) {
            rpcReference = new RpcReference(new JavassistProxyFactory());
        } else {
            rpcReference = new RpcReference(new JDKProxyFactory());
        }
        return rpcReference;
    }

    /**
     * 启动服务之前需要预先订阅对应的dubbo服务
     *
     * @param serviceBean
     */
    public void doSubscribeService(Class serviceBean) {
        if (abstractRegister == null) {
            abstractRegister = new ZookeeperRegister(clientConfig.getRegisterAddr());
        }
        URL url = new URL();
        url.setApplicationName(clientConfig.getApplicationName());
        url.setServiceName(serviceBean.getName());
        url.addParameter("host", CommonUtils.getIpAddress());
        abstractRegister.subscribe(url);
    }

    /**
     * 开始和各个provider建立连接
     */
    public void doConnectServer() {
        for (URL providerURL : SUBSCRIBE_SERVICE_LIST) {
            List<String> providerIps = abstractRegister.getProviderIps(providerURL.getServiceName());
            for (String providerIp : providerIps) {
                try {
                    ConnectionHandler.connect(providerURL.getServiceName(), providerIp);
                } catch (InterruptedException e) {
                    logger.error("[doConnectServer] connect fail ", e);
                }
            }
            URL url = new URL();
            url.addParameter("servicePath",providerURL.getServiceName()+"/provider");
            url.addParameter("providerIps", JSON.toJSONString(providerIps));
            // 这个函数内部需要订阅每个Provider目录下节点的变化信息，以及Provider目录下每个节点自身的数据变动情况
            abstractRegister.doAfterSubscribe(url);
        }
    }


    /**
     * 开启发送线程
     * 开启发送线程，专门从事将数据包发送到服务端，起到一个解耦的效果
     *
     * @param
     */
    public void startClient() {
        Thread asyncSendJob = new Thread(new AsyncSendJob());
        asyncSendJob.start();
    }


    public static void main(String[] args) throws Throwable {
        Client client = new Client();
        RpcReference rpcReference = client.initClientApplication();
        // 初始化客户端配置，比如 路由策略
        client.initClientConfig();
        // 获取代理对象，设置缓存信息，用订阅时调用
        IDataService dataService = rpcReference.get(IDataService.class);
        // 订阅某个服务，添加本地缓存SUBSCRIBE_SERVICE_LIST
        client.doSubscribeService(IDataService.class);
        ConnectionHandler.setBootstrap(client.getBootstrap());
        // 订阅服务，从SUBSCRIBE_SERVICE_LIST中获取需要订阅的服务信息，添加注册中心的监听
        // 根据服务生产者信息，建立连接ChannelFuture，建立的ChannelFuture放入CONNECT_MAP
        client.doConnectServer();
        // 开启异步线程，发送函数请求，通过队列SEND_QUEUE进行通信
        client.startClient();
        for (int i = 0; i < 100; i++) {
            // 被代理层invoke方法，增强功能（拦截），将请求放入队列SEND_QUEUE中
            // 异步线程asyncSendJob接收到SEND_QUEUE数据，发起netty调用；在invoke方法中3*1000时间内"死循环获取"RESP_MAP缓存中的响应数据
            // 在ClientHandler中将请求方法，将响应数据放入RESP_MAP中
            String result = dataService.sendData("test");
            System.out.println(result);
            Thread.sleep(1000);
        }
    }

    /**
     * 后续可以考虑加入spi
     *
     */
    private void initClientConfig() {
        // 初始化路由策略
        String routerStrategy = clientConfig.getRouterStrategy();
        if (RANDOM_ROUTER_TYPE.equals(routerStrategy)) {
            IROUTER = new RandomRouterImpl();
        } else if (ROTATE_ROUTER_TYPE.equals(routerStrategy)){
            IROUTER = new RotateRouterImpl();
        }
    }


    /**
     * 异步发送信息任务
     */
    class AsyncSendJob implements Runnable {
        public AsyncSendJob() {
        }

        @Override
        public void run() {
            while (true) {
                try {
                    // 阻塞模式，取走BlockingQueue里排在首位的对象,若BlockingQueue为空,
                    // 阻断进入等待状态直到Blocking有新的对象被加入为止
                    RpcInvocation data = SEND_QUEUE.take();
                    // 将RpcInvocation封装成RpcProtocol对象中，然后发送给服务端，这里正好对应了上文中的ServerHandler
                    String json = JSON.toJSONString(data);
                    RpcProtocol rpcProtocol = new RpcProtocol(json.getBytes());
                    ChannelFuture channelFuture = ConnectionHandler.getChannelFuture(data.getTargetServiceName());
                    //netty的通道负责发送数据给服务端
                    channelFuture.channel().writeAndFlush(rpcProtocol);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

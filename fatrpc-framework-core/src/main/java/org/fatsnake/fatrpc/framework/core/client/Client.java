package org.fatsnake.fatrpc.framework.core.client;

import com.alibaba.fastjson.JSON;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import org.fatsnake.fatrpc.framework.core.common.RpcDecoder;
import org.fatsnake.fatrpc.framework.core.common.RpcEncoder;
import org.fatsnake.fatrpc.framework.core.common.RpcInvocation;
import org.fatsnake.fatrpc.framework.core.common.RpcProtocol;
import org.fatsnake.fatrpc.framework.core.common.config.ClientConfig;
import org.fatsnake.fatrpc.framework.core.common.config.PropertiesBootstrap;
import org.fatsnake.fatrpc.framework.core.common.event.IRpcListenerLoader;
import org.fatsnake.fatrpc.framework.core.common.utils.CommonUtils;
import org.fatsnake.fatrpc.framework.core.filter.IClientFilter;
import org.fatsnake.fatrpc.framework.core.filter.client.ClientFilterChain;
import org.fatsnake.fatrpc.framework.core.proxy.IProxyFactory;
import org.fatsnake.fatrpc.framework.core.registy.RegistryService;
import org.fatsnake.fatrpc.framework.core.registy.URL;
import org.fatsnake.fatrpc.framework.core.registy.zookeeper.AbstractRegister;
import org.fatsnake.fatrpc.framework.core.router.IRouter;
import org.fatsnake.fatrpc.framework.core.serialize.SerializeFactory;
import org.fatsnake.fatrpc.framework.interfaces.DataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.fatsnake.fatrpc.framework.core.common.cache.CommonClientCache.*;
import static org.fatsnake.fatrpc.framework.core.common.constans.RpcConstants.DEFAULT_DECODE_CHAR;
import static org.fatsnake.fatrpc.framework.core.spi.ExtensionLoader.EXTENSION_LOADER_CLASS_CACHE;


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

    public RpcReference initClientApplication() throws IOException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        EventLoopGroup clientGroup = new NioEventLoopGroup();
        bootstrap.group(clientGroup);
        bootstrap.channel(NioSocketChannel.class);
        bootstrap.handler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) throws Exception {
//                ByteBuf delimiter = Unpooled.copiedBuffer(DEFAULT_DECODE_CHAR.getBytes());
//                ch.pipeline().addLast(new DelimiterBasedFrameDecoder(8192, delimiter));
                ch.pipeline().addLast(new RpcEncoder());
                ch.pipeline().addLast(new RpcDecoder());
                ch.pipeline().addLast(new ClientHandler());
            }
        });
        iRpcListenerLoader = new IRpcListenerLoader();
        iRpcListenerLoader.init();
        // 初始化客户端应用信息
        this.clientConfig = PropertiesBootstrap.loadClientConfigFromLocal();
        CLIENT_CONFIG = this.clientConfig;

        // 使用spi的方式加载代理类
        // spi扩展的加载部分
        this.initClientConfig();
        EXTENSION_LOADER.loadExtension(IProxyFactory.class);
        String proxyType = clientConfig.getProxyType();
        LinkedHashMap<String, Class> classMap = EXTENSION_LOADER_CLASS_CACHE.get(IProxyFactory.class.getName());
        Class proxyClassType = classMap.get(proxyType);
        IProxyFactory proxyFactory = (IProxyFactory) proxyClassType.newInstance();
        return new RpcReference(proxyFactory);
    }

    /**
     * 启动服务之前需要预先订阅对应的dubbo服务
     *
     * @param serviceBean
     */
    public void doSubscribeService(Class serviceBean) {
        // spi方式，获取注册中心，订阅服务
        if (ABSTRACT_REGISTER == null) {
            try {
                EXTENSION_LOADER.loadExtension(RegistryService.class);
                Map<String, Class> registerMap = EXTENSION_LOADER_CLASS_CACHE.get(RegistryService.class.getName());
                Class registerClass = registerMap.get(clientConfig.getRegisterType());
                ABSTRACT_REGISTER = (AbstractRegister) registerClass.newInstance();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        URL url = new URL();
        url.setApplicationName(clientConfig.getApplicationName());
        url.setServiceName(serviceBean.getName());
        url.addParameter("host", CommonUtils.getIpAddress());
        // 获取服务的权重信息
        Map<String, String> result = ABSTRACT_REGISTER.getServiceWeightMap(serviceBean.getName());
        URL_MAP.put(serviceBean.getName(), result);
        ABSTRACT_REGISTER.subscribe(url);
    }

    /**
     * 开始和各个provider建立连接
     */
    public void doConnectServer() {
        for (URL providerURL : SUBSCRIBE_SERVICE_LIST) {
            List<String> providerIps = ABSTRACT_REGISTER.getProviderIps(providerURL.getServiceName());
            for (String providerIp : providerIps) {
                try {
                    ConnectionHandler.connect(providerURL.getServiceName(), providerIp);
                } catch (InterruptedException e) {
                    logger.error("[doConnectServer] connect fail ", e);
                }
            }
            URL url = new URL();
            url.addParameter("servicePath", providerURL.getServiceName() + "/provider");
            url.addParameter("providerIps", JSON.toJSONString(providerIps));
            // 这个函数内部需要订阅每个Provider目录下节点的变化信息，以及Provider目录下每个节点自身的数据变动情况
            ABSTRACT_REGISTER.doAfterSubscribe(url);
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
        // 塞入包装类参数（用于过滤链）和 反射生成对象
        RpcReferenceWrapper<DataService> rpcReferenceWrapper = new RpcReferenceWrapper<>();
        rpcReferenceWrapper.setAimClass(DataService.class);
        rpcReferenceWrapper.setGroup("dev");
        rpcReferenceWrapper.setServiceToken("token-a");
        // 获取代理对象，设置缓存信息，用订阅时调用
        DataService dataService = rpcReference.get(rpcReferenceWrapper);
        // 订阅某个服务，添加本地缓存SUBSCRIBE_SERVICE_LIST
        client.doSubscribeService(DataService.class);
        ConnectionHandler.setBootstrap(client.getBootstrap());
        // 订阅服务，从SUBSCRIBE_SERVICE_LIST中获取需要订阅的服务信息，添加注册中心的监听
        // 根据服务生产者信息，建立连接ChannelFuture，建立的ChannelFuture放入CONNECT_MAP
        client.doConnectServer();
        // 开启异步线程，发送函数请求，通过队列SEND_QUEUE进行通信
        client.startClient();
        for (int i = 0; i < 10000; i++) {
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
     */
    private void initClientConfig() throws IOException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        // spi方式，初始化路由策略
        EXTENSION_LOADER.loadExtension(IRouter.class);
        String routerStrategy = clientConfig.getRouterStrategy();
        LinkedHashMap<String, Class> iRouterMap = EXTENSION_LOADER_CLASS_CACHE.get(IRouter.class.getName());
        Class iRouterClass = iRouterMap.get(routerStrategy);
        if (iRouterClass == null) {
            throw new RuntimeException("no match routerStrategy for" + routerStrategy);
        }
        IROUTER = (IRouter) iRouterClass.newInstance();

        // spi方式，初始化序列化策略
        EXTENSION_LOADER.loadExtension(SerializeFactory.class);
        String clientSerialize = clientConfig.getClientSerialize();
        LinkedHashMap<String, Class> serializeMap = EXTENSION_LOADER_CLASS_CACHE.get(SerializeFactory.class.getName());
        Class serializeFactoryClass = serializeMap.get(clientSerialize);
        if (serializeFactoryClass == null) {
            throw new RuntimeException("no match serialize type for " + clientSerialize);
        }
        CLIENT_SERIALIZE_FACTORY = (SerializeFactory) serializeFactoryClass.newInstance();

        // spi方式，初始化过滤链 指定过滤顺序
        EXTENSION_LOADER.loadExtension(IClientFilter.class);
        ClientFilterChain clientFilterChain = new ClientFilterChain();
        LinkedHashMap<String, Class> iClientMap = EXTENSION_LOADER_CLASS_CACHE.get(IClientFilter.class.getName());
        for (String impClassName : iClientMap.keySet()) {
            Class iClientFilterClass = iClientMap.get(impClassName);
            if (iClientFilterClass == null) {
                throw new RuntimeException("no match iClientFilter for " + iClientFilterClass);
            }
            clientFilterChain.addClientFilter((IClientFilter) iClientFilterClass.newInstance());
        }
        CLIENT_FILTER_CHAIN = clientFilterChain;
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
                    RpcInvocation rpcInvocation = SEND_QUEUE.take();
                    // 将RpcInvocation封装成RpcProtocol对象中，然后发送给服务端，这里正好对应了上文中的ServerHandler
//                    String json = JSON.toJSONString(data);
//                    RpcProtocol rpcProtocol = new RpcProtocol(json.getBytes());
                    ChannelFuture channelFuture = ConnectionHandler.getChannelFuture(rpcInvocation);
                    if (channelFuture != null) {
                        RpcProtocol rpcProtocol = new RpcProtocol(CLIENT_SERIALIZE_FACTORY.serialize(rpcInvocation));
                        //netty的通道负责发送数据给服务端
                        channelFuture.channel().writeAndFlush(rpcProtocol);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

package org.fatsnake.fatrpc.framework.core.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.sctp.nio.NioSctpServerChannel;
import io.netty.channel.socket.SocketChannel;
import org.fatsnake.fatrpc.framework.core.common.RpcDecoder;
import org.fatsnake.fatrpc.framework.core.common.RpcEncoder;
import org.fatsnake.fatrpc.framework.core.common.config.PropertiesBootstrap;
import org.fatsnake.fatrpc.framework.core.common.config.ServerConfig;
import org.fatsnake.fatrpc.framework.core.common.utils.CommonUtils;
import org.fatsnake.fatrpc.framework.core.filter.server.ServerFilterChain;
import org.fatsnake.fatrpc.framework.core.filter.server.ServerLogFilterImpl;
import org.fatsnake.fatrpc.framework.core.filter.server.ServerTokenFilterImpl;
import org.fatsnake.fatrpc.framework.core.registy.RegistryService;
import org.fatsnake.fatrpc.framework.core.registy.URL;
import org.fatsnake.fatrpc.framework.core.registy.zookeeper.ZookeeperRegister;
import org.fatsnake.fatrpc.framework.core.serialize.fastjson.FastJsonSerializeFactory;
import org.fatsnake.fatrpc.framework.core.serialize.hessian.HessianSerializeFactory;
import org.fatsnake.fatrpc.framework.core.serialize.jdk.JdkSerializeFactory;
import org.fatsnake.fatrpc.framework.core.serialize.kryo.KryoSerializeFactory;
import static org.fatsnake.fatrpc.framework.core.common.cache.CommonServerCache.*;
import static org.fatsnake.fatrpc.framework.core.common.constans.RpcConstants.*;

/**
 * @Auther: fatsnake
 * @Description": 服务端通信模型
 * @Date:2022/7/3 3:42 下午
 * Copyright (c) 2022, zaodao All Rights Reserved.
 */
public class Server {

    private static EventLoopGroup bossGroup = null;

    private static EventLoopGroup workerGroup = null;

    private ServerConfig serverConfig;

    private RegistryService registryService;


    public ServerConfig getServerConfig() {
        return serverConfig;
    }

    public void setServerConfig(ServerConfig serverConfig) {
        this.serverConfig = serverConfig;
    }

    public void startApplication() throws InterruptedException {
        bossGroup = new NioEventLoopGroup();
        workerGroup = new NioEventLoopGroup();
        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(bossGroup, workerGroup);
        bootstrap.channel(NioSctpServerChannel.class);
        bootstrap.option(ChannelOption.TCP_NODELAY, true);
        bootstrap.option(ChannelOption.SO_BACKLOG, 1024);
        bootstrap.option(ChannelOption.SO_SNDBUF, 16 * 1024)
                .option(ChannelOption.SO_RCVBUF, 16 * 1024)
                .option(ChannelOption.SO_KEEPALIVE, true);

        bootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) throws Exception {
                System.out.println("初始化provider过程");
                ch.pipeline().addLast(new RpcEncoder());
                ch.pipeline().addLast(new RpcDecoder());
                ch.pipeline().addLast(new ServerHandler());
            }
        });
        // 将服务端的具体服务都暴露到注册中心，方便客户端进行调用
        this.batchExportUrl();
        bootstrap.bind(serverConfig.getServerPort()).sync();
    }


    public void initServerConfig() {
        // 这个对象主要是负责将properties的配置转换成本地的一个Map结构进行管理。
        ServerConfig serverConfig = PropertiesBootstrap.loadServerConfigFromLocal();
        this.setServerConfig(serverConfig);
        // 初始化序列化策略
        String serverSerialize = serverConfig.getServerSerialize();
        switch (serverSerialize) {
            case JDK_SERIALIZE_TYPE:
                SERVER_SERIALIZE_FACTORY = new JdkSerializeFactory();
                break;
            case FAST_JSON_SERIALIZE_TYPE:
                SERVER_SERIALIZE_FACTORY = new FastJsonSerializeFactory();
                break;
            case HESSIAN2_SERIALIZE_TYPE:
                SERVER_SERIALIZE_FACTORY = new HessianSerializeFactory();
                break;
            case KRYO_SERIALIZE_TYPE:
                SERVER_SERIALIZE_FACTORY = new KryoSerializeFactory();
                break;
            default:
                throw new RuntimeException("no match serialize type for" + serverSerialize);
        }
        System.out.println("serverSerialize is "+serverSerialize);
        SERVER_CONFIG = serverConfig;
        // 初始化服务端调用链 ，确定顺序
        ServerFilterChain serverFilterChain = new ServerFilterChain();
        serverFilterChain.addServerFilter(new ServerLogFilterImpl());
        serverFilterChain.addServerFilter(new ServerTokenFilterImpl());
        SERVER_FILTER_CHAIN = serverFilterChain;

    }


    /**
     * 暴露服务信息
     *
     * @param serviceBean
     */
    public void exportService(Object serviceBean) {
        if (serviceBean.getClass().getInterfaces().length == 0) {
            throw new RuntimeException("service must had interfaces!");
        }
        Class[] classes = serviceBean.getClass().getInterfaces();
        if (classes.length > 1) {
            throw new RuntimeException("service must only had one interfaces!");
        }
        if (REGISTRY_SERVICE == null) {
            REGISTRY_SERVICE = new ZookeeperRegister(serverConfig.getRegisterAddr());
        }
        // 默认选择该对象的第一个实现接口
        Class interfaceClass = classes[0];
        PROVIDER_CLASS_MAP.put(interfaceClass.getName(), serviceBean);
        URL url = new URL();
        url.setServiceName(interfaceClass.getName());
        url.setApplicationName(serverConfig.getApplicationName());
        url.addParameter("host", CommonUtils.getIpAddress());
        url.addParameter("port", String.valueOf(serverConfig.getServerPort()));
        PROVIDER_URL_SET.add(url);
    }


    /**
     * 为了将服务端的具体服务都暴露到注册中心，方便客户端进行调用。
     */
    public void batchExportUrl() {
        Thread task = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                for (URL url : PROVIDER_URL_SET) {
                    REGISTRY_SERVICE.register(url);
                }
            }
        });
        task.start();
    }


    public void registryService(Object serviceBean) {
        if (serviceBean.getClass().getInterfaces().length == 0) {
            throw new RuntimeException("service must had interfaces！");
        }
        Class[] classes = serviceBean.getClass().getInterfaces();
        if (classes.length > 1) {
            throw new RuntimeException("service must only had one interfaces!");
        }
        Class interfaceClass = classes[0];
        PROVIDER_CLASS_MAP.put(interfaceClass.getName(), serviceBean);
    }

    public static void main(String[] args) throws InterruptedException {
//        Server server = new Server();
//        ServerConfig serverConfig = new ServerConfig();
//        serverConfig.setPort(9090);
//        server.setServerConfig(serverConfig);
//        server.registryService(new DataService());
//        server.startApplication();

        // 服务配置改为外部化
        Server server = new Server();
        server.initServerConfig();
        // 初始化registryService，需要注册的服务信息封装URL对象，添加到PROVIDER_CLASS_MAP中，等待启动时注册到注册中心
        server.exportService(new DataService());
        server.startApplication();

    }
}

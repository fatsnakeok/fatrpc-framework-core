package org.fatsnake.fatrpc.framework.core.common.config;

import java.io.IOException;

import static org.fatsnake.fatrpc.framework.core.common.constans.RpcConstants.CLIENT_DEFAULT_MSG_LENGTH;
import static org.fatsnake.fatrpc.framework.core.common.constans.RpcConstants.DEFAULT_MAX_CONNECTION_NUMS;
import static org.fatsnake.fatrpc.framework.core.common.constans.RpcConstants.DEFAULT_QUEUE_SIZE;
import static org.fatsnake.fatrpc.framework.core.common.constans.RpcConstants.DEFAULT_THREAD_NUMS;
import static org.fatsnake.fatrpc.framework.core.common.constans.RpcConstants.DEFAULT_TIMEOUT;
import static org.fatsnake.fatrpc.framework.core.common.constans.RpcConstants.JDK_PROXY_TYPE;
import static org.fatsnake.fatrpc.framework.core.common.constans.RpcConstants.JDK_SERIALIZE_TYPE;
import static org.fatsnake.fatrpc.framework.core.common.constans.RpcConstants.RANDOM_ROUTER_TYPE;
import static org.fatsnake.fatrpc.framework.core.common.constans.RpcConstants.SERVER_DEFAULT_MSG_LENGTH;

/**
 * @Author fatsnake
 * @Date created in 10:46 上午 2021/12/12
 */
public class PropertiesBootstrap {

    private volatile boolean configIsReady;

    public static final String SERVER_PORT = "fatrpc.serverPort";
    public static final String REGISTER_ADDRESS = "fatrpc.registerAddr";
    public static final String REGISTER_TYPE = "fatrpc.registerType";
    public static final String APPLICATION_NAME = "fatrpc.applicationName";
    public static final String PROXY_TYPE = "fatrpc.proxyType";
    public static final String ROUTER_TYPE = "fatrpc.router";
    public static final String SERVER_SERIALIZE_TYPE = "fatrpc.serverSerialize";
    public static final String CLIENT_SERIALIZE_TYPE = "fatrpc.clientSerialize";
    public static final String CLIENT_DEFAULT_TIME_OUT = "fatrpc.client.default.timeout";
    public static final String SERVER_BIZ_THREAD_NUMS = "fatrpc.server.biz.thread.nums";
    public static final String SERVER_QUEUE_SIZE = "fatrpc.server.queue.size";
    public static final String MAX_CONNECTION = "fatrpc.server.macx.connection";
    public static final String SERVER_MAX_DATA_SIZE = "fatrpc.server.max.data.size";
    public static final String CLIENT_MAX_DATA_SIZE = "fatrpc.client.max.data.size";

    public static ServerConfig loadServerConfigFromLocal() {
        try {
            PropertiesLoader.loadConfiguration();
        } catch (IOException e) {
            throw new RuntimeException("loadServerConfigFromLocal fail,e is {}", e);
        }
        ServerConfig serverConfig = new ServerConfig();
        serverConfig.setServerPort(PropertiesLoader.getPropertiesInteger(SERVER_PORT));
        serverConfig.setApplicationName(PropertiesLoader.getPropertiesStr(APPLICATION_NAME));
        serverConfig.setRegisterAddr(PropertiesLoader.getPropertiesStr(REGISTER_ADDRESS));
        serverConfig.setRegisterType(PropertiesLoader.getPropertiesStr(REGISTER_TYPE));
        serverConfig.setServerSerialize(PropertiesLoader.getPropertiesStrDefault(SERVER_SERIALIZE_TYPE, JDK_SERIALIZE_TYPE));
        serverConfig.setServerBizThreadNums(PropertiesLoader.getPropertiesIntegerDefault(SERVER_BIZ_THREAD_NUMS, DEFAULT_THREAD_NUMS));
        serverConfig.setServerQueueSize(PropertiesLoader.getPropertiesIntegerDefault(SERVER_QUEUE_SIZE,DEFAULT_QUEUE_SIZE));
        serverConfig.setServerQueueSize(PropertiesLoader.getPropertiesIntegerDefault(MAX_CONNECTION,DEFAULT_MAX_CONNECTION_NUMS));
        serverConfig.setMaxServerRequestData(PropertiesLoader.getPropertiesIntegerDefault(SERVER_MAX_DATA_SIZE,SERVER_DEFAULT_MSG_LENGTH));
        return serverConfig;
    }

    public static ClientConfig loadClientConfigFromLocal() {
        try {
            PropertiesLoader.loadConfiguration();
        } catch (IOException e) {
            throw new RuntimeException("loadClientConfigFromLocal fail,e is {}", e);
        }
        ClientConfig clientConfig = new ClientConfig();
        clientConfig.setApplicationName(PropertiesLoader.getPropertiesNotBlank(APPLICATION_NAME));
        clientConfig.setRegisterAddr(PropertiesLoader.getPropertiesNotBlank(REGISTER_ADDRESS));
        clientConfig.setRegisterType(PropertiesLoader.getPropertiesStr(REGISTER_TYPE));
        clientConfig.setProxyType(PropertiesLoader.getPropertiesStrDefault(PROXY_TYPE, JDK_PROXY_TYPE));
        clientConfig.setRouterStrategy(PropertiesLoader.getPropertiesStrDefault(ROUTER_TYPE, RANDOM_ROUTER_TYPE));
        clientConfig.setClientSerialize(PropertiesLoader.getPropertiesStrDefault(CLIENT_SERIALIZE_TYPE, JDK_SERIALIZE_TYPE));
        clientConfig.setTimeOut(PropertiesLoader.getPropertiesIntegerDefault(CLIENT_DEFAULT_TIME_OUT,DEFAULT_TIMEOUT));
        clientConfig.setMaxServerRespDataSize(PropertiesLoader.getPropertiesIntegerDefault(CLIENT_MAX_DATA_SIZE,CLIENT_DEFAULT_MSG_LENGTH));
        return clientConfig;
    }

}

package org.fatsnake.fatrpc.framework.core.common.config;

/**
 * @Auther: fatsnake
 * @Description":
 * @Date:2022/7/3 4:01 下午
 * Copyright (c) 2022, zaodao All Rights Reserved.
 */
public class ClientConfig {

    private String applicationName;

    private String registerAddr;

    /**
     * 代理类型： example: jdk, javassist
     */
    private String proxyType;

    /**
     * 负载均衡策略：example：random, rotate
     */
    private String routerStrategy;

    /**
     *  户端序列化方式 example: hession2,kryo,jdk,fastjson
     */
    private String clientSerialize;

    public String getClientSerialize() {
        return clientSerialize;
    }

    public void setClientSerialize(String clientSerialize) {
        this.clientSerialize = clientSerialize;
    }

    public String getRouterStrategy() {
        return routerStrategy;
    }

    public void setRouterStrategy(String routerStrategy) {
        this.routerStrategy = routerStrategy;
    }

    public String getApplicationName() {
        return applicationName;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

    public String getRegisterAddr() {
        return registerAddr;
    }

    public void setRegisterAddr(String registerAddr) {
        this.registerAddr = registerAddr;
    }

    public String getProxyType() {
        return proxyType;
    }

    public void setProxyType(String proxyType) {
        this.proxyType = proxyType;
    }
}

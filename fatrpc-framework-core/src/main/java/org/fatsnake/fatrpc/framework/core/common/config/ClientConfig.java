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

    private String registerType;

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

    /**
     * 客户端数据的超时时间
     */
    private Integer timeOut;

    /**
     * 客户端最大响应数据体积
     */
    private Integer maxServerRespDataSize;

    public Integer getMaxServerRespDataSize() {
        return maxServerRespDataSize;
    }

    public void setMaxServerRespDataSize(Integer maxServerRespDataSize) {
        this.maxServerRespDataSize = maxServerRespDataSize;
    }

    public Integer getTimeOut() {
        return timeOut;
    }

    public void setTimeOut(Integer timeOut) {
        this.timeOut = timeOut;
    }

    public String getRegisterType() {
        return registerType;
    }

    public void setRegisterType(String registerType) {
        this.registerType = registerType;
    }

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

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

    private String proxyType;

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

package org.fatsnake.fatrpc.framework.core.common.config;

/**
 * @Auther: fatsnake
 * @Description":
 * @Date:2022/7/3 4:01 下午
 * Copyright (c) 2022, zaodao All Rights Reserved.
 */
public class ClientConfig {

    /**
     * 端口
     */
    private Integer port;

    /**
     * 服务地址
     */
    private String serverAddr;

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getServerAddr() {
        return serverAddr;
    }

    public void setServerAddr(String serverAddr) {
        this.serverAddr = serverAddr;
    }
}

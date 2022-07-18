package org.fatsnake.fatrpc.framework.core.common.config;

/**
 * @Auther: fatsnake
 * @Description":
 * @Date:2022/7/3 4:03 下午
 * Copyright (c) 2022, zaodao All Rights Reserved.
 */
public class ServerConfig {

    private Integer serverPort;

    private String registerAddr;

    private String applicationName;

    private String serverSerialize;

    /**
     * 服务端序列化方式 example: hession2,kryo,jdk,fastjson
     * @return String
     */
    public String getServerSerialize() {
        return serverSerialize;
    }

    public void setServerSerialize(String serverSerialize) {
        this.serverSerialize = serverSerialize;
    }

    public String getRegisterAddr() {
        return registerAddr;
    }

    public void setRegisterAddr(String registerAddr) {
        this.registerAddr = registerAddr;
    }

    public String getApplicationName() {
        return applicationName;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

    public Integer getServerPort() {
        return serverPort;
    }

    public void setServerPort(Integer serverPort) {
        this.serverPort = serverPort;
    }
}

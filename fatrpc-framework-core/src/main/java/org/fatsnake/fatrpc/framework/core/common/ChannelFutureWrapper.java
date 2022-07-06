package org.fatsnake.fatrpc.framework.core.common;

import io.netty.channel.ChannelFuture;

/**
 * @Auther: fatsnake
 * @Description":
 * @Date:2022/7/6 17:19
 * Copyright (c) 2022, zaodao All Rights Reserved.
 */
public class ChannelFutureWrapper {
    private ChannelFuture channelFuture;

    private String host;

    private Integer port;

    public ChannelFuture getChannelFuture() {
        return channelFuture;
    }

    public void setChannelFuture(ChannelFuture channelFuture) {
        this.channelFuture = channelFuture;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }
}

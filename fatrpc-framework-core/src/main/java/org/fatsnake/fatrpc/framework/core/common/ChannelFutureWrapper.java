package org.fatsnake.fatrpc.framework.core.common;

import io.netty.channel.ChannelFuture;

/**
 * @Auther: fatsnake
 * @Description": 是一个自定义的包装类，将netty建立好的ChannelFuture做了一些封装
 * @Date:2022/7/6 17:19
 * Copyright (c) 2022, zaodao All Rights Reserved.
 */
public class ChannelFutureWrapper {
    private ChannelFuture channelFuture;

    private String host;

    private Integer port;

    private Integer weight;

    private String group;

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public ChannelFutureWrapper(String host, Integer port, Integer weight) {
        this.host = host;
        this.port = port;
        this.weight = weight;
    }

    public ChannelFutureWrapper() {
    }

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

    @Override
    public String toString() {
        return "ChannelFutureWrapper{" +
                "channelFuture=" + channelFuture +
                ", host='" + host + '\'' +
                ", port=" + port +
                ", weight=" + weight +
                '}';
    }
}

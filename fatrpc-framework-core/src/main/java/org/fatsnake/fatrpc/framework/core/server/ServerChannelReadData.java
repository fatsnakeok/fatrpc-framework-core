package org.fatsnake.fatrpc.framework.core.server;

import io.netty.channel.ChannelHandlerContext;
import org.fatsnake.fatrpc.framework.core.common.RpcDecoder;
import org.fatsnake.fatrpc.framework.core.common.RpcProtocol;

/**
 * @Auther: fatsnake
 * @Description":  业务堵塞队列传输对象
 * @Date:2022/7/20 6:56 上午
 * Copyright (c) 2022, zaodao All Rights Reserved.
 */
public class ServerChannelReadData {

    private RpcProtocol rpcProtocol;

    private ChannelHandlerContext channelHandlerContext;

    public RpcProtocol getRpcProtocol() {
        return rpcProtocol;
    }

    public void setRpcProtocol(RpcProtocol rpcProtocol) {
        this.rpcProtocol = rpcProtocol;
    }

    public ChannelHandlerContext getChannelHandlerContext() {
        return channelHandlerContext;
    }

    public void setChannelHandlerContext(ChannelHandlerContext channelHandlerContext) {
        this.channelHandlerContext = channelHandlerContext;
    }
}

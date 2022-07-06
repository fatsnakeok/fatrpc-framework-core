package org.fatsnake.fatrpc.framework.core.common;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * @Auther: fatsnake
 * @Description": RPC 请求编码器，发送数据之前会通过此模块
 * @Date:2022/7/3 4:41 下午
 * Copyright (c) 2022, zaodao All Rights Reserved.
 */
public class RpcEncoder extends MessageToByteEncoder<RpcProtocol> {


    @Override
    protected void encode(ChannelHandlerContext ctx, RpcProtocol msg, ByteBuf out) throws Exception {
        out.writeShort(msg.getMagicNumber());
        out.writeInt(msg.getContentLength());
        out.writeBytes(msg.getContent());
    }
}

package org.fatsnake.fatrpc.framework.core.common;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * @Auther: fatsnake
 * @Description":
 * @Date:2022/7/3 4:22 下午
 * Copyright (c) 2022, zaodao All Rights Reserved.
 */
public class RpcDecoder extends ByteToMessageDecoder {

    /**
     * 协议开头的标准长度
     */
    public final int BASE_LENGTH = 2 +4;


    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {




    }
}

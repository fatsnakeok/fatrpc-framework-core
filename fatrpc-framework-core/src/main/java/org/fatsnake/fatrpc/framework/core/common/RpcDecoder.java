package org.fatsnake.fatrpc.framework.core.common;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

import static org.fatsnake.fatrpc.framework.core.common.constans.RpcConstants.MAGIC_NUMBER;

/**
 * @Auther: fatsnake
 * @Description": RPC 解码器
 * @Date:2022/7/3 4:22 下午
 * Copyright (c) 2022, zaodao All Rights Reserved.
 */
public class RpcDecoder extends ByteToMessageDecoder {

    /**
     * 协议开头的标准长度
     */
    public final int BASE_LENGTH = 2 + 4;


    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf byteBuf, List<Object> out) throws Exception {
        if (byteBuf.readableBytes() >= BASE_LENGTH) {
            // 防止收到一些体积过大的数据包
            if (byteBuf.readableBytes() > 1000) {
                byteBuf.skipBytes(byteBuf.readableBytes());
            }
            int beginReader;
            while (true) {
                beginReader = byteBuf.readerIndex();
                byteBuf.markReaderIndex();
                // 比对RpcProtocol的魔法数，确认是否合法请求
                if (byteBuf.readShort() == MAGIC_NUMBER) {
                    break;
                } else {
                    // 不是魔法数开头，说明是非法的客户端发来的数据包
                    ctx.close();
                    return;
                }
            }

            // 对应了RpcProtocol对象的contentLength字段
            int length = byteBuf.readInt();
            // 说明剩余的数据包不是完整的，这里需要重置下读索引
            if (byteBuf.readableBytes() < length) {
                byteBuf.readerIndex(beginReader);
                return;
            }
            // 其实是实际RpcProtocol对象的content字段
            byte[] data = new byte[length];
            byteBuf.readBytes(data);
            RpcProtocol rpcProtocol = new RpcProtocol(data);
            out.add(rpcProtocol);
        }


    }
}

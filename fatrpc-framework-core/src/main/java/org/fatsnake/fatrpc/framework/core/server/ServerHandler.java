package org.fatsnake.fatrpc.framework.core.server;

import com.alibaba.fastjson.JSON;
import com.sun.xml.internal.ws.api.addressing.AddressingVersion;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.fatsnake.fatrpc.framework.core.common.RpcInvocation;
import org.fatsnake.fatrpc.framework.core.common.RpcProtocol;

import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.prefs.PreferenceChangeEvent;

import static org.fatsnake.fatrpc.framework.core.common.cache.CommonServerCache.PROVIDER_CLASS_MAP;

/**
 * @Auther: fatsnake
 * @Description":
 * @Date:2022/7/3 4:43 下午
 * Copyright (c) 2022, zaodao All Rights Reserved.
 */
public class ServerHandler extends ChannelInboundHandlerAdapter {


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        RpcProtocol rpcProtocol = (RpcProtocol) msg;
        String json = new String(rpcProtocol.getContent(), 0, rpcProtocol.getContentLength());
        RpcInvocation rpcInvocation = JSON.parseObject(json, RpcInvocation.class);
        Object aimObject = PROVIDER_CLASS_MAP.get(rpcInvocation.getTargetServiceName());
        Method[] methods = aimObject.getClass().getDeclaredMethods();
        Object result = null;
        for (Method method : methods) {
            if (method.getName().equals(rpcInvocation.getTargetMethod())) {
                if (method.getReturnType().equals(Void.TYPE)) {
                    method.invoke(aimObject, rpcInvocation.getArgs());
                } else {
                    result = method.invoke(aimObject, rpcInvocation.getArgs());
                }
                break;
            }
        }

        rpcInvocation.setResponse(result);
        RpcProtocol respRpcProtocol = new RpcProtocol(JSON.toJSONString(rpcProtocol).getBytes(StandardCharsets.UTF_8));
        ctx.writeAndFlush(respRpcProtocol);
    }


    /**
     * 异常捕获
     *
     * @param ctx   ctx
     * @param cause cause
     * @throws Exception Exception
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        Channel channel = ctx.channel();
        if (channel.isActive()) {
            ctx.close();
        }
    }
}

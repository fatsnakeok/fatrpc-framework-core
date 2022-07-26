package org.fatsnake.fatrpc.framework.core.server;

import com.alibaba.fastjson.JSON;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.fatsnake.fatrpc.framework.core.common.RpcInvocation;
import org.fatsnake.fatrpc.framework.core.common.RpcProtocol;

import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.Semaphore;

import static org.fatsnake.fatrpc.framework.core.common.cache.CommonServerCache.PROVIDER_CLASS_MAP;
import static org.fatsnake.fatrpc.framework.core.common.cache.CommonServerCache.SERVER_CHANNEL_DISPATCHER;
import static org.fatsnake.fatrpc.framework.core.common.cache.CommonServerCache.SERVER_SERIALIZE_FACTORY;

/**
 * @Auther: fatsnake
 * @Description":
 * @Date:2022/7/3 4:43 下午
 * Copyright (c) 2022, zaodao All Rights Reserved.
 */
public class ServerHandler extends ChannelInboundHandlerAdapter {


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg)  {
//        RpcProtocol rpcProtocol = (RpcProtocol) msg;
////        String json = new String(rpcProtocol.getContent(), 0, rpcProtocol.getContentLength());
////        RpcInvocation rpcInvocation = JSON.parseObject(json, RpcInvocation.class);
//        RpcInvocation rpcInvocation =SERVER_SERIALIZE_FACTORY.deserialize(rpcProtocol.getContent(),RpcInvocation.class);
//
//        // 执行过滤链
//        // 插入原因
//        // 在 ChannelInboundHandlerAdapter 内部加入过滤链说明此事请求数据已经落入到了server端的业务线程池中，
//        // 接下来需要通过责任链的每一个环节进行校对，最终确认是否可以执行目标函数。
//        SERVER_FILTER_CHAIN.doFilter(rpcInvocation);
//        Object aimObject = PROVIDER_CLASS_MAP.get(rpcInvocation.getTargetServiceName());
//        Method[] methods = aimObject.getClass().getDeclaredMethods();
//        Object result = null;
//        for (Method method : methods) {
//            if (method.getName().equals(rpcInvocation.getTargetMethod())) {
//                if (method.getReturnType().equals(Void.TYPE)) {
//                    method.invoke(aimObject, rpcInvocation.getArgs());
//                } else {
//                    result = method.invoke(aimObject, rpcInvocation.getArgs());
//                }
//                break;
//            }
//        }
//
//        rpcInvocation.setResponse(result);
////        RpcProtocol respRpcProtocol = new RpcProtocol(JSON.toJSONString(rpcProtocol).getBytes(StandardCharsets.UTF_8));
//        RpcProtocol respRpcProtocol = new RpcProtocol(SERVER_SERIALIZE_FACTORY.serialize(rpcInvocation));
//        ctx.writeAndFlush(respRpcProtocol);

        ServerChannelReadData serverChannelReadData = new ServerChannelReadData();
        serverChannelReadData.setRpcProtocol((RpcProtocol) msg);
        serverChannelReadData.setChannelHandlerContext(ctx);
        // 对象进行简单封装，放入队列中，在业务线程池中进行处理
        SERVER_CHANNEL_DISPATCHER.add(serverChannelReadData);

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

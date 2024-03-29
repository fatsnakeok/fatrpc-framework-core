package org.fatsnake.fatrpc.framework.core.client;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;
import io.netty.util.internal.StringUtil;
import org.fatsnake.fatrpc.framework.core.common.RpcInvocation;
import org.fatsnake.fatrpc.framework.core.common.RpcProtocol;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.fatsnake.fatrpc.framework.core.common.cache.CommonClientCache.CLIENT_SERIALIZE_FACTORY;
import static org.fatsnake.fatrpc.framework.core.common.cache.CommonClientCache.RESP_MAP;

/**
 * @Author fatsnake
 * @Date created in 8:21 下午 2021/11/24
 */
public class  ClientHandler extends ChannelInboundHandlerAdapter {

    private static Logger logger = LoggerFactory.getLogger(ClientHandler.class);

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        // 客户端和服务端之间的数据都是以RpcProtocol对象作为基本协议进行的交互
        RpcProtocol rpcProtocol = (RpcProtocol) msg;
        byte[] reqContent = rpcProtocol.getContent();
        // 这里是传输参数是更为详细的RpcInvocation对象字节数组
        RpcInvocation rpcInvocation = CLIENT_SERIALIZE_FACTORY.deserialize(reqContent, RpcInvocation.class);
        if (rpcInvocation.getE() != null) {
            rpcInvocation.getE().printStackTrace();
        }
        // 如果单纯异步模式的话，响应Map集合中不存在映射值
        Object r = rpcInvocation.getAttachments().get("async");
        if (r != null && Boolean.valueOf(String.valueOf(r))) {
            ReferenceCountUtil.release(msg);
            return;
        }
        // 通过之前发送的uuid来注入匹配的响应数值
        if(!RESP_MAP.containsKey(rpcInvocation.getUuid())){
            throw new IllegalArgumentException("server response is error!");
        }
        //将请求的响应结构放入一个Map集合中，集合的key就是uuid，这个uuid在发送请求之前就已经初始化好了，
        // 所以只需要起一个线程在后台遍历这个map，查看对应的key是否有相应即可。
        //uuid何时放入到map？其实放入的操作我将它封装到了代理类中进行实现。
        RESP_MAP.put(rpcInvocation.getUuid(),rpcInvocation);
        ReferenceCountUtil.release(msg);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
        Channel channel = ctx.channel();
        if(channel.isActive()){
            System.err.println(cause);
            ctx.close();
        }
    }
}

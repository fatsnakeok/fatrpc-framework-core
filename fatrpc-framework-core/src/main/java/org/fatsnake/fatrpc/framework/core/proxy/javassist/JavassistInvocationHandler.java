package org.fatsnake.fatrpc.framework.core.proxy.javassist;


import org.fatsnake.fatrpc.framework.core.client.RpcReferenceWrapper;
import org.fatsnake.fatrpc.framework.core.common.RpcInvocation;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.UUID;
import java.util.concurrent.TimeoutException;
import static org.fatsnake.fatrpc.framework.core.common.cache.CommonClientCache.RESP_MAP;
import static org.fatsnake.fatrpc.framework.core.common.cache.CommonClientCache.SEND_QUEUE;
import static org.fatsnake.fatrpc.framework.core.common.constans.RpcConstants.DEFAULT_TIMEOUT;


/**
 * @Author fatsnake
 * @Date created in 7:15 下午 2021/12/5
 */
public class JavassistInvocationHandler implements InvocationHandler {


    private final static Object OBJECT = new Object();

    private RpcReferenceWrapper rpcReferenceWrapper;

    private Long timeOut = Long.valueOf(DEFAULT_TIMEOUT);


    public JavassistInvocationHandler(RpcReferenceWrapper rpcReferenceWrapper) {
        this.rpcReferenceWrapper = rpcReferenceWrapper;
        timeOut = Long.valueOf(String.valueOf(rpcReferenceWrapper.getAttatchments().get("timeOut")));
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        RpcInvocation rpcInvocation = new RpcInvocation();
        rpcInvocation.setArgs(args);
        rpcInvocation.setTargetMethod(method.getName());
        rpcInvocation.setTargetServiceName(rpcReferenceWrapper.getAimClass().getName());
        rpcInvocation.setAttachments(rpcReferenceWrapper.getAttatchments());
        rpcInvocation.setUuid(UUID.randomUUID().toString());
        RESP_MAP.put(rpcInvocation.getUuid(), OBJECT);
        //代理类内部将请求放入到发送队列中，等待发送队列发送请求
        SEND_QUEUE.add(rpcInvocation);
        if (rpcReferenceWrapper.isAsync()) {
            return null;
        }
        long beginTime = System.currentTimeMillis();
        //如果请求数据在指定时间内返回则返回给客户端调用方
        while (System.currentTimeMillis() - beginTime < timeOut) {
            Object object = RESP_MAP.get(rpcInvocation.getUuid());
            if (object instanceof RpcInvocation) {
                return ((RpcInvocation) object).getResponse();
            }
        }
        // 修改抛出异常的信息
        throw new TimeoutException("Wait for response from server on client " + timeOut + "ms,Service's name is " + rpcInvocation.getTargetServiceName() + "#" + rpcInvocation.getTargetMethod());
    }
}

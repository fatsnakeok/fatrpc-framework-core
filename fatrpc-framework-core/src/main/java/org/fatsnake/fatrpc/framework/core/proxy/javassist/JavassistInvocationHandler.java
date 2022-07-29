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
        // 设置重试次数初始值
        rpcInvocation.setRetry(rpcReferenceWrapper.getRetry());

        //代理类内部将请求放入到发送队列中，等待发送队列发送请求
        SEND_QUEUE.add(rpcInvocation);
        if (rpcReferenceWrapper.isAsync()) {
            return null;
        }
        // 占个位置
        RESP_MAP.put(rpcInvocation.getUuid(), OBJECT);
        long beginTime = System.currentTimeMillis();
        int retryTimes = 0;
        //如果请求数据在指定时间内返回则返回给客户端调用方
        while (System.currentTimeMillis() - beginTime < timeOut
                || rpcInvocation.getRetry() > 0) {
            Object object = RESP_MAP.get(rpcInvocation.getUuid());
            if (object != null && object instanceof RpcInvocation) {
                RpcInvocation rpcInvocationResp = (RpcInvocation) object;
                // 正常结果
                if(rpcInvocationResp.getRetry() == 0 || rpcInvocationResp.getRetry() != 0 && rpcInvocationResp.getE() == null){
                    // 删除已使用的响应结果
                    RESP_MAP.remove(rpcInvocation.getUuid());
                    return rpcInvocationResp.getResponse();
                } else if (rpcInvocation.getE() != null) {
                    if (rpcInvocation.getRetry() == 0) {
                        return rpcInvocationResp.getResponse();
                    }
                    // 如果是因为超时的情况，才会触发重试规则，否则重试机制不生效
                    if (System.currentTimeMillis() - beginTime > timeOut) {
                        retryTimes++;
                        // 重新请求
                        rpcInvocation.setResponse(null);
                        //每次重试之后都会将retry值扣减1
                        rpcInvocation.setRetry(rpcInvocation.getRetry() - 1);
                        RESP_MAP.put(rpcInvocation.getUuid(), OBJECT);
                        SEND_QUEUE.add(rpcInvocation);
                    }
                }
            }
        }
        // 应对一些请求超时的情况
        RESP_MAP.remove(rpcInvocation.getUuid());
        // 修改抛出异常的信息
        throw new TimeoutException("Wait for response from server on client " + timeOut + "ms,Service's name is " + rpcInvocation.getTargetServiceName() + "#" + rpcInvocation.getTargetMethod());
    }
}

package org.fatsnake.fatrpc.framework.core.proxy.jdk;

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
 * @Auther: fatsnake
 * @Description": 各种代理工程使用同一个InvocationHandler
 * @Date:2022/7/4 13:17
 * Copyright (c) 2022, zaodao All Rights Reserved.
 * <p>
 * <p>
 * 核心任务就是将需要调用的方法名称、服务名称，参数统统都封装好到RpcInvocation当中，
 * 然后塞入到一个队列里，并且等待服务端的数据返回。
 */
public class JDKClientInvocationHandler implements InvocationHandler {


    private final static Object OBJECT = new Object();

    private RpcReferenceWrapper rpcReferenceWrapper;

    private int timeOut = DEFAULT_TIMEOUT;

    public JDKClientInvocationHandler(RpcReferenceWrapper rpcReferenceWrapper) {
        this.rpcReferenceWrapper = rpcReferenceWrapper;
        timeOut = Integer.valueOf(String.valueOf(rpcReferenceWrapper.getAttatchments().get("timeOut")));
    }


    /**
     * 在代码片段中我们需要对每次请求都增加一个uuid进行区分，
     * 这样可以将请求和响应进行关联匹配，方便我们在客户端接收数据的时候进行识别。
     *
     * @param proxy
     * @param method
     * @param args
     * @return
     * @throws Throwable
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        RpcInvocation rpcInvocation = new RpcInvocation();
        rpcInvocation.setArgs(args);
        rpcInvocation.setTargetMethod(method.getName());
        rpcInvocation.setTargetServiceName(rpcReferenceWrapper.getAimClass().getName());
        //这里面注入了一个uuid，对每一次的请求都做单独区分
        rpcInvocation.setUuid(UUID.randomUUID().toString());
        // 设置参数，用于责任链校验
        rpcInvocation.setAttachments(rpcReferenceWrapper.getAttatchments());
        // 为什么不是  RESP_MAP.put(rpcInvocation.getUuid(), rpcInvocation);？
        // invoke内部有一个while循环的逻辑，它会不断地从RESP_MAP中提取响应结果，
        //  如果对应的结果是RpcInvocation类型才会进行解析，否则会继续循环等待
        RESP_MAP.put(rpcInvocation.getUuid(), OBJECT);

        // 设置重试次数初始值
        rpcInvocation.setRetry(rpcReferenceWrapper.getRetry());
        //这里就是将请求的参数放入到发送队列中
        SEND_QUEUE.add(rpcInvocation);
        if (rpcReferenceWrapper.isAsync()) {
            return null;
        }
        RESP_MAP.put(rpcInvocation.getUuid(), OBJECT);
        long beginTime = -System.currentTimeMillis();
        int retryTimes = 0;
        //客户端请求超时的一个判断依据
        while (System.currentTimeMillis() - beginTime < timeOut || rpcInvocation.getRetry() > 0) {
            Object object = RESP_MAP.get(rpcInvocation.getUuid());
            if (object instanceof RpcInvocation) {
                RpcInvocation rpcInvocationResp = (RpcInvocation) object;
                // 正常结果
                if (rpcInvocationResp.getRetry() == 0 && rpcInvocationResp.getE() == null) {
                    return rpcInvocationResp.getResponse();
                } else if (rpcInvocationResp.getE() != null) {
                    // 每次重试之后都会将retry值扣减1
                    if (rpcInvocationResp.getRetry() == 0) {
                        return rpcInvocationResp.getResponse();
                    }
                    // 如果是因为超时的情况，才会触发重试的规则，否则重试机制不生效
                    if (System.currentTimeMillis() - beginTime > timeOut) {
                        retryTimes++;
                        //重新请求
                        rpcInvocation.setResponse(null);
                        rpcInvocation.setRetry(rpcInvocationResp.getRetry() - 1);
                        RESP_MAP.put(rpcInvocation.getUuid(), OBJECT);
                        SEND_QUEUE.add(rpcInvocation);
                    }
                }
            }
        }
        //防止key一直存在于map集合中
        RESP_MAP.remove(rpcInvocation.getUuid());
        // 修改错误信息
        throw new TimeoutException("wait for response from server on client " + timeOut + "ms!");
    }
}

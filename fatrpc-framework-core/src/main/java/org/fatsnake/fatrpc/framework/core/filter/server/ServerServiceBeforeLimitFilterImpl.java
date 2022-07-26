package org.fatsnake.fatrpc.framework.core.filter.server;

import org.fatsnake.fatrpc.framework.core.common.RpcInvocation;
import org.fatsnake.fatrpc.framework.core.common.ServerServiceSemaphoreWrapper;
import org.fatsnake.fatrpc.framework.core.common.annotations.SPI;
import org.fatsnake.fatrpc.framework.core.common.exception.MaxServiceLimitRequestException;
import org.fatsnake.fatrpc.framework.core.filter.IServerFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Semaphore;

import static org.fatsnake.fatrpc.framework.core.common.cache.CommonServerCache.SERVER_SERVICE_SEMAPHORE_MAP;


/**
 * 服务端方法限流过滤器
 *
 * @Author linhao
 * @Date created in 11:10 上午 2022/3/6
 */
@SPI("before")
public class ServerServiceBeforeLimitFilterImpl implements IServerFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(ServerServiceBeforeLimitFilterImpl.class);

    @Override
    public void doFilter(RpcInvocation rpcInvocation) {
        String serviceName = rpcInvocation.getTargetServiceName();
        ServerServiceSemaphoreWrapper serverServiceSemaphoreWrapper = SERVER_SERVICE_SEMAPHORE_MAP.get(serviceName);
        //从缓存中提取semaphore对象
        Semaphore semaphore = serverServiceSemaphoreWrapper.getSemaphore();
        boolean tryResult = semaphore.tryAcquire();
        if (!tryResult) {
            LOGGER.error("[ServerServiceBeforeLimitFilterImpl] {}'s max request is {},reject now", rpcInvocation.getTargetServiceName(), serverServiceSemaphoreWrapper.getMaxNums());
            MaxServiceLimitRequestException iRpcException = new MaxServiceLimitRequestException(rpcInvocation);
            rpcInvocation.setE(iRpcException);
            throw iRpcException;
        }
    }
}

package org.fatsnake.fatrpc.framework.core.filter.server;


import org.fatsnake.fatrpc.framework.core.common.RpcInvocation;
import org.fatsnake.fatrpc.framework.core.common.ServerServiceSemaphoreWrapper;
import org.fatsnake.fatrpc.framework.core.common.annotations.SPI;
import org.fatsnake.fatrpc.framework.core.filter.IServerFilter;

import static org.fatsnake.fatrpc.framework.core.common.cache.CommonServerCache.SERVER_SERVICE_SEMAPHORE_MAP;

/**
 * @Author linhao
 * @Date created in 11:52 上午 2022/3/6
 */
@SPI("after")
public class ServerServiceAfterLimitFilterImpl implements IServerFilter {

    @Override
    public void doFilter(RpcInvocation rpcInvocation) {
        String serviceName = rpcInvocation.getTargetServiceName();
        ServerServiceSemaphoreWrapper serverServiceSemaphoreWrapper = SERVER_SERVICE_SEMAPHORE_MAP.get(serviceName);
        serverServiceSemaphoreWrapper.getSemaphore().release();
    }
}

package org.fatsnake.fatrpc.framework.core.common;

import javax.print.DocFlavor;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

import static org.fatsnake.fatrpc.framework.core.common.cache.CommonClientCache.SERVICE_ROUTER_MAP;

/**
 * @Auther: fatsnake
 * @Description":
 * @Date:2022/7/11 11:33
 * Copyright (c) 2022, zaodao All Rights Reserved.
 */
public class ChannelFuturePollingRef {

    private AtomicLong referenceTimes = new AtomicLong(0);

    /**
     * 获取原子递增一个数，与数组长度取余，计算出数据下标，用于轮询获取数据
     *
     * @param serviceName
     * @return
     */
    public ChannelFutureWrapper getChannelFutureWrapper(String serviceName) {
        ChannelFutureWrapper[] arr = SERVICE_ROUTER_MAP.get(serviceName);
        long i = referenceTimes.getAndIncrement(); // 原子递增一个
        int index = (int) (i % arr.length);
        return arr[index];
    }

}

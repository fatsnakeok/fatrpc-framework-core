package org.fatsnake.fatrpc.framework.core.filter.client;

import org.fatsnake.fatrpc.framework.core.common.ChannelFutureWrapper;
import org.fatsnake.fatrpc.framework.core.common.RpcInvocation;
import org.fatsnake.fatrpc.framework.core.filter.IClientFilter;

import java.util.ArrayList;
import java.util.List;

/**
 * @Auther: fatsnake
 * @Description": 客户端过滤链路类
 * 将过滤器连接起来
 * 过滤链的实例需要在服务启动中有一个统一存储的内存区域
 * @Date:2022/7/16 10:35 上午
 * Copyright (c) 2022, zaodao All Rights Reserved.
 */
public class ClientFilterChain {

    private static List<IClientFilter> iClientFilterList = new ArrayList<>();

    /**
     * 添加过滤器
     *
     * @param iClientFilter
     */
    public void addClientFilter(IClientFilter iClientFilter) {
        iClientFilterList.add(iClientFilter);
    }

    /**
     * 循环调用过滤器链的过滤器
     *
     * @param src
     * @param rpcInvocation
     */
    public void doFilter(List<ChannelFutureWrapper> src, RpcInvocation rpcInvocation) {
        for (IClientFilter iClientFilter : iClientFilterList) {
            iClientFilter.doFilter(src, rpcInvocation);
        }
    }
}

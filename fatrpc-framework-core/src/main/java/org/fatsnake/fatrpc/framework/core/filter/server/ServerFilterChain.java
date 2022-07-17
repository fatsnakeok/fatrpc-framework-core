package org.fatsnake.fatrpc.framework.core.filter.server;

import org.fatsnake.fatrpc.framework.core.common.RpcInvocation;
import org.fatsnake.fatrpc.framework.core.filter.IServerFilter;

import java.util.ArrayList;
import java.util.List;

/**
 * @Auther: fatsnake
 * @Description": 服务器端过滤器链
 * 过滤链的实例需要在服务启动中有一个统一存储的内存区域
 * @Date:2022/7/16 10:43 上午
 * Copyright (c) 2022, zaodao All Rights Reserved.
 */
public class ServerFilterChain {

    private static List<IServerFilter> iServerFilterList = new ArrayList<>();

    public void addServerFilter(IServerFilter iserverFilter) {
        iServerFilterList.add(iserverFilter);
    }

    public void doFilter(RpcInvocation rpcInvocation) {
        for (IServerFilter iServerFilter : iServerFilterList) {
            iServerFilter.doFilter(rpcInvocation);
        }
    }

}

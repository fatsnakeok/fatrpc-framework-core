package org.fatsnake.fatrpc.framework.core.filter;

import org.fatsnake.fatrpc.framework.core.common.ChannelFutureWrapper;
import org.fatsnake.fatrpc.framework.core.common.RpcInvocation;

import java.util.List;

/**
 * @Auther: fatsnake
 * @Description": 客户端过滤器
 * @Date:2022/7/16 10:21 上午
 * Copyright (c) 2022, zaodao All Rights Reserved.
 */
public interface IClientFilter extends IFilter{

    /**
     * 执行过滤器链
     * @param src
     * @param rpcInvocation
     */
    void doFilter(List<ChannelFutureWrapper> src, RpcInvocation rpcInvocation);

}

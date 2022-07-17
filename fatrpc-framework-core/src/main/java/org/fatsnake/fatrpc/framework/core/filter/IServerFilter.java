package org.fatsnake.fatrpc.framework.core.filter;

import org.fatsnake.fatrpc.framework.core.common.RpcInvocation;

/**
 * @Auther: fatsnake
 * @Description": 服务端过滤器
 * @Date:2022/7/16 10:30 上午
 * Copyright (c) 2022, zaodao All Rights Reserved.
 */
public interface IServerFilter extends IFilter{

    /**
     * 执行核心过滤逻辑
     * @param rpcInvocation
     */
    void doFilter(RpcInvocation rpcInvocation);

}

package org.fatsnake.fatrpc.framework.core.filter.client;

import org.fatsnake.fatrpc.framework.core.common.ChannelFutureWrapper;
import org.fatsnake.fatrpc.framework.core.common.RpcInvocation;
import org.fatsnake.fatrpc.framework.core.common.utils.CommonUtils;
import org.fatsnake.fatrpc.framework.core.filter.IClientFilter;

import java.util.List;

/**
 * 服务分组过滤器
 * @Auther: fatsnake
 * @Description": 通过rpcInvocation内部的attachment这个map结构来实现
 * @Date:2022/7/16 11:07 上午
 * Copyright (c) 2022, zaodao All Rights Reserved.
 */
public class GroupFilterImpl implements IClientFilter {


    @Override
    public void doFilter(List<ChannelFutureWrapper> src, RpcInvocation rpcInvocation) {
        String group = String.valueOf(rpcInvocation.getAttachments().get("group"));
        for (ChannelFutureWrapper channelFutureWrapper : src) {
            if (!channelFutureWrapper.getGroup().equals(group)) {
                src.remove(channelFutureWrapper);
            }
        }
        if (CommonUtils.isEmptyList(src)) {
            throw new RuntimeException("no provider match for group" + group);
        }
    }
}

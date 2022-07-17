package org.fatsnake.fatrpc.framework.core.filter.client;

import org.fatsnake.fatrpc.framework.core.common.ChannelFutureWrapper;
import org.fatsnake.fatrpc.framework.core.common.RpcInvocation;
import org.fatsnake.fatrpc.framework.core.common.utils.CommonUtils;
import org.fatsnake.fatrpc.framework.core.filter.IClientFilter;

import java.util.Iterator;
import java.util.List;

/**
 * @Auther: fatsnake
 * @Description":
 * @Date:2022/7/16 11:40 上午
 * Copyright (c) 2022, zaodao All Rights Reserved.
 */
public class DirectInvokeFilterImpl implements IClientFilter {
    @Override
    public void doFilter(List<ChannelFutureWrapper> src, RpcInvocation rpcInvocation) {
        String url = (String) rpcInvocation.getAttachments().get("url");
        if (CommonUtils.isEmpty(url)) {
            return;
        }
        Iterator<ChannelFutureWrapper> channelFutureWrapperIterator = src.iterator();
        while (channelFutureWrapperIterator.hasNext()) {
            ChannelFutureWrapper channelFutureWrapper = channelFutureWrapperIterator.next();
            if (!(channelFutureWrapper.getHost()+":"+channelFutureWrapper.getPort()).equals(url)) {
                channelFutureWrapperIterator.remove();
            }
        }
        if (CommonUtils.isEmptyList(src)) {
            throw new RuntimeException("no match provider url for "+ url);
        }
    }
}

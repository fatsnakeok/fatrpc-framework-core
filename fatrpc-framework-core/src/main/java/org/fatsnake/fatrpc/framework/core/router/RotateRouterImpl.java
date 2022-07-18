package org.fatsnake.fatrpc.framework.core.router;

import org.fatsnake.fatrpc.framework.core.common.ChannelFutureWrapper;
import org.fatsnake.fatrpc.framework.core.registy.URL;

import java.util.List;

import static org.fatsnake.fatrpc.framework.core.common.cache.CommonClientCache.CHANNEL_FUTURE_POLLING_REF;
import static org.fatsnake.fatrpc.framework.core.common.cache.CommonClientCache.CONNECT_MAP;
import static org.fatsnake.fatrpc.framework.core.common.cache.CommonClientCache.SERVICE_ROUTER_MAP;

/**
 * @Auther: fatsnake
 * @Description":  轮询调用实现
 * @Date:2022/7/12 11:26
 * Copyright (c) 2022, zaodao All Rights Reserved.
 */
public class RotateRouterImpl implements IRouter{
    @Override
    public void refreshRouterArr(Selector selector) {
        List<ChannelFutureWrapper> channelFutureWrapperList = CONNECT_MAP.get(selector.getProviderServiceName());
        ChannelFutureWrapper[] arr = new ChannelFutureWrapper[channelFutureWrapperList.size()];
        for (int i = 0; i < channelFutureWrapperList.size(); i++) {
            arr[i] = channelFutureWrapperList.get(i);
        }
         SERVICE_ROUTER_MAP.put(selector.getProviderServiceName(), arr);
    }

    @Override
    public ChannelFutureWrapper select(Selector selector) {
        return CHANNEL_FUTURE_POLLING_REF.getChannelFutureWrapper(selector.getProviderServiceName());
    }

    @Override
    public void updateWeight(URL url) {

    }
}

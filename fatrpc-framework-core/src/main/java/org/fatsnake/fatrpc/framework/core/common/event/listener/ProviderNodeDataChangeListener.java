package org.fatsnake.fatrpc.framework.core.common.event.listener;

import org.fatsnake.fatrpc.framework.core.common.ChannelFutureWrapper;
import org.fatsnake.fatrpc.framework.core.common.event.IRpcNodeChangeEvent;
import org.fatsnake.fatrpc.framework.core.registy.URL;
import org.fatsnake.fatrpc.framework.core.registy.zookeeper.ProviderNodeInfo;

import java.util.List;

import static org.fatsnake.fatrpc.framework.core.common.cache.CommonClientCache.CONNECT_MAP;
import static org.fatsnake.fatrpc.framework.core.common.cache.CommonClientCache.IROUTER;

/**
 * @Auther: fatsnake
 * @Description": 更新权重的事件监听器
 * @Date:2022/7/12 11:05
 * Copyright (c) 2022, zaodao All Rights Reserved.
 */
public class ProviderNodeDataChangeListener implements IRpcListener<IRpcNodeChangeEvent> {
    @Override
    public void callBack(Object t) {
        ProviderNodeInfo providerNodeInfo = ((ProviderNodeInfo) t);
        List<ChannelFutureWrapper> channelFutureWrapperList = CONNECT_MAP.get(providerNodeInfo.getServiceName());
        for (ChannelFutureWrapper channelFutureWrapper : channelFutureWrapperList) {
            String address = channelFutureWrapper.getHost() + ":" + channelFutureWrapper.getPort();
            if (address.equals(providerNodeInfo.getAddress())) {
                channelFutureWrapper.setGroup(providerNodeInfo.getGroup());
                // 修改权重
                channelFutureWrapper.setWeight(providerNodeInfo.getWeight());
                URL url = new URL();
                url.setServiceName(providerNodeInfo.getServiceName());
                // 更新权重
                IROUTER.updateWeight(url);
                break;
            }
        }
    }
}

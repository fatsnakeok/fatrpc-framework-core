package org.fatsnake.fatrpc.framework.core.common.event.listener;

import io.netty.channel.ChannelFuture;
import org.fatsnake.fatrpc.framework.core.client.ConnectionHandler;
import org.fatsnake.fatrpc.framework.core.common.ChannelFutureWrapper;
import org.fatsnake.fatrpc.framework.core.common.event.FatRpcListener;
import org.fatsnake.fatrpc.framework.core.common.event.FatRpcUpdateEvent;
import org.fatsnake.fatrpc.framework.core.common.event.data.URLChangeWrapper;
import org.fatsnake.fatrpc.framework.core.common.utils.CommonUtils;
import org.fatsnake.fatrpc.framework.core.registy.URL;
import org.fatsnake.fatrpc.framework.core.registy.zookeeper.ProviderNodeInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.fatsnake.fatrpc.framework.core.common.cache.CommonClientCache.CONNECT_MAP;

/**
 * @Auther: fatsnake
 * @Description": ZK服务器提供者节点发生了变更，需要发送通知操作，更新本地一个目标服务列表，避免向无用的服务发送请求
 * @Date:2022/7/8 10:14 下午
 * Copyright (c) 2022, zaodao All Rights Reserved.
 */
public class ServiceUpdateListener implements FatRpcListener<FatRpcUpdateEvent> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceUpdateListener.class);


    @Override
    public void callBack(Object t) {
        // 获取到节点的数据信息
        URLChangeWrapper urlChangeWrapper = (URLChangeWrapper) t;
        List<ChannelFutureWrapper> channelFutureWrapperList = CONNECT_MAP.get(urlChangeWrapper.getServiceName());
        if (CommonUtils.isEmptyList(channelFutureWrapperList)) {
            LOGGER.error("[ServiceUpdateListener] channelFutureWrappers is empty");
            return;
        } else {
            List<String> matchProviderUrl = urlChangeWrapper.getProviderUrl();
            Set<String> finalUrl = new HashSet<>();
            List<ChannelFutureWrapper> finalChannelFutureWrappers = new ArrayList<>();
            for (ChannelFutureWrapper channelFutureWrapper : channelFutureWrapperList) {
                String oldServiceAddress = channelFutureWrapper.getHost() + ":" + channelFutureWrapper.getPort();
                // 如果老的url没有，说明已经被移除了
                if (!matchProviderUrl.contains(oldServiceAddress)) {
                    continue;
                } else {
                    finalChannelFutureWrappers.add(channelFutureWrapper);
                    finalUrl.add(oldServiceAddress);
                }
            }
            // 此时老的url已经被移除了，开始检查是否有新的url
            //ChannelFutureWrapper其实是一个自定义的包装类，将netty建立好的ChannelFuture做了一些封装
            List<ChannelFutureWrapper> newChannelFutureWrapper = new ArrayList<>();
            for (String newProviderUrl : matchProviderUrl) {
                if (!finalUrl.contains(newProviderUrl)) {
                    ChannelFutureWrapper channelFutureWrapper = new ChannelFutureWrapper();
                    String host = newProviderUrl.split(":")[0];
                    Integer port = Integer.valueOf(newProviderUrl.split(":")[1]);
                    channelFutureWrapper.setPort(port);
                    channelFutureWrapper.setHost(host);
                    String urlStr = urlChangeWrapper.getNodeDataUrl().get(newProviderUrl);
                    ProviderNodeInfo providerNodeInfo = URL.buildURLFromUrlStr(urlStr);
                    channelFutureWrapper.setWeight(providerNodeInfo.getWeight());
                    channelFutureWrapper.setGroup(providerNodeInfo.getGroup());
                    ChannelFuture channelFuture = null;
                    try {
                        channelFuture = ConnectionHandler.createChannelFuture(host, port);
                        channelFutureWrapper.setChannelFuture(channelFuture);
                        newChannelFutureWrapper.add(channelFutureWrapper);
                        finalUrl.add(newProviderUrl);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            finalChannelFutureWrappers.addAll(newChannelFutureWrapper);
            // 最终更新服务在这里
            CONNECT_MAP.put(urlChangeWrapper.getServiceName(), finalChannelFutureWrappers);
        }
    }
}

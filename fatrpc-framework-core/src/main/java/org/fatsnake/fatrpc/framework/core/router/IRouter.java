package org.fatsnake.fatrpc.framework.core.router;


import io.netty.channel.ChannelFuture;
import org.fatsnake.fatrpc.framework.core.common.ChannelFutureWrapper;
import org.fatsnake.fatrpc.framework.core.registy.URL;

/**
 * @Auther: fatsnake
 * @Description": 路由层操作统一接口
 * @Date:2022/7/11 11:09
 * Copyright (c) 2022, zaodao All Rights Reserved.
 */
public interface IRouter {

    /**
     * 刷新路由数据
     *
     * @param selector
     */
    void refreshRouterArr(Selector selector);


    /**
     * 获取请求到连接通道
     *
     * @param selector
     * @return
     */
    ChannelFutureWrapper select(Selector selector);


    /**
     * 更新权重信息
     *
     * @param url
     */
    void updateWeight(URL url);


}

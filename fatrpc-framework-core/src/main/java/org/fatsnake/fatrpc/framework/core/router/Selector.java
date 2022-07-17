package org.fatsnake.fatrpc.framework.core.router;

import org.fatsnake.fatrpc.framework.core.common.ChannelFutureWrapper;

/**
 * @Auther: fatsnake
 * @Description":
 * @Date:2022/7/11 11:10
 * Copyright (c) 2022, zaodao All Rights Reserved.
 */
public class Selector {

    /**
     * 服务名
     * eg: com.fat.test.DataService
     */
    private String providerServiceName;

    /**
     * 经过二次筛选之后的future集合
     */
    private ChannelFutureWrapper[] channelFutureWrappers;

    public ChannelFutureWrapper[] getChannelFutureWrappers() {
        return channelFutureWrappers;
    }

    public void setChannelFutureWrappers(ChannelFutureWrapper[] channelFutureWrappers) {
        this.channelFutureWrappers = channelFutureWrappers;
    }

    public String getProviderServiceName() {
        return providerServiceName;
    }

    public void setProviderServiceName(String providerServiceName) {
        this.providerServiceName = providerServiceName;
    }
}

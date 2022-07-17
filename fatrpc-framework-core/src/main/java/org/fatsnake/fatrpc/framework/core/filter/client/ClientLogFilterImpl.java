package org.fatsnake.fatrpc.framework.core.filter.client;

import org.fatsnake.fatrpc.framework.core.common.ChannelFutureWrapper;
import org.fatsnake.fatrpc.framework.core.common.RpcInvocation;
import org.fatsnake.fatrpc.framework.core.filter.IClientFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static org.fatsnake.fatrpc.framework.core.common.cache.CommonClientCache.CLIENT_CONFIG;

/**
 * @Auther: fatsnake
 * @Description": 记录请求链路的信息
 * @Date:2022/7/16 11:01 上午
 * Copyright (c) 2022, zaodao All Rights Reserved.
 */
public class ClientLogFilterImpl implements IClientFilter {

    private static Logger logger = LoggerFactory.getLogger(ClientLogFilterImpl.class);

    @Override
    public void doFilter(List<ChannelFutureWrapper> src, RpcInvocation rpcInvocation) {
        rpcInvocation.getAttachments().put("c_app_name", CLIENT_CONFIG.getApplicationName());
        logger.info(rpcInvocation.getAttachments().get("c_app_name")+ "do invoke ---->" + rpcInvocation.getTargetServiceName());
    }
}

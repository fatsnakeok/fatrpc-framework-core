package org.fatsnake.fatrpc.framework.core.filter.server;

import org.fatsnake.fatrpc.framework.core.common.RpcInvocation;
import org.fatsnake.fatrpc.framework.core.common.utils.CommonUtils;
import org.fatsnake.fatrpc.framework.core.filter.IServerFilter;
import org.fatsnake.fatrpc.framework.core.server.ServiceWrapper;

import static org.fatsnake.fatrpc.framework.core.common.cache.CommonServerCache.PROVIDER_SERVICE_WRAPPER_MAP;

/**
 * 乞丐版的token校验
 * @Auther: fatsnake
 * @Description":
 * @Date:2022/7/16 11:49 上午
 * Copyright (c) 2022, zaodao All Rights Reserved.
 */
public class ServerTokenFilterImpl implements IServerFilter {
    @Override
    public void doFilter(RpcInvocation rpcInvocation) {
        String token = String.valueOf(rpcInvocation.getAttachments().get("serviceToken"));
        // 目前鉴权只是简单一个Map作为判断依据
        ServiceWrapper serviceWrapper = PROVIDER_SERVICE_WRAPPER_MAP.get(rpcInvocation.getTargetServiceName());
        String matchToken = String.valueOf(serviceWrapper.getServiceToken());
        if (CommonUtils.isEmpty(matchToken)) {
            return;
        }
        if (!CommonUtils.isEmpty(token) && token.equals(matchToken)) {
            return;
        }
        throw new RuntimeException("token is " + token + " , verify result is false!");
    }
}

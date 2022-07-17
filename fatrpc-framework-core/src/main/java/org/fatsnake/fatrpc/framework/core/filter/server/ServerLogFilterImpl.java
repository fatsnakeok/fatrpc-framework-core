package org.fatsnake.fatrpc.framework.core.filter.server;

import org.fatsnake.fatrpc.framework.core.common.RpcInvocation;
import org.fatsnake.fatrpc.framework.core.filter.IServerFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 服务端日志记录过滤器
 * @Auther: fatsnake
 * @Description":
 * @Date:2022/7/16 11:46 上午
 * Copyright (c) 2022, zaodao All Rights Reserved.
 */
public class ServerLogFilterImpl implements IServerFilter {

    private static Logger logger = LoggerFactory.getLogger(ServerLogFilterImpl.class);

    @Override
    public void doFilter(RpcInvocation rpcInvocation) {
        logger.info(rpcInvocation.getAttachments().get("c_app_name") + "do invoke ---->"
                + rpcInvocation.getTargetServiceName() + "#" + rpcInvocation.getTargetMethod());
    }
}

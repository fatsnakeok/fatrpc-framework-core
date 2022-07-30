package org.fatsnake.fatrpc.framework.core.common.exception;

import org.fatsnake.fatrpc.framework.core.common.RpcInvocation;

/**
 * @Auther: fatsnake
 * @Description":
 * @Date:2022/7/26 6:37 上午
 * Copyright (c) 2022, zaodao All Rights Reserved.
 */
public class FatRpcException extends RuntimeException{
    private RpcInvocation rpcInvocation;

    public RpcInvocation getRpcInvocation() {
        return rpcInvocation;
    }

    public void setRpcInvocation(RpcInvocation rpcInvocation) {
        this.rpcInvocation = rpcInvocation;
    }

    public FatRpcException(RpcInvocation rpcInvocation) {
        this.rpcInvocation = rpcInvocation;
    }
}

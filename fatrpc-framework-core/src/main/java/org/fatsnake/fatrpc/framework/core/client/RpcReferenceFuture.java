package org.fatsnake.fatrpc.framework.core.client;

/**
 * @Auther: fatsnake
 * @Description":
 * @Date:2022/7/21 6:31 上午
 * Copyright (c) 2022, zaodao All Rights Reserved.
 */
public class RpcReferenceFuture<T> {

    private RpcReferenceWrapper rpcReferenceWrapper;

    private Object response;

    public Object getResponse() {
        return response;
    }

    public void setResponse(Object response) {
        this.response = response;
    }

    public RpcReferenceWrapper getRpcReferenceWrapper() {
        return rpcReferenceWrapper;
    }

    public void setRpcReferenceWrapper(RpcReferenceWrapper rpcReferenceWrapper) {
        this.rpcReferenceWrapper = rpcReferenceWrapper;
    }
}

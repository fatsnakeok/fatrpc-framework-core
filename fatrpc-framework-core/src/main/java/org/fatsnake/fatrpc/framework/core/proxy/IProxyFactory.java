package org.fatsnake.fatrpc.framework.core.proxy;

import org.fatsnake.fatrpc.framework.core.client.RpcReferenceWrapper;

/**
 * @Auther: fatsnake
 * @Description":
 * @Date:2022/7/4 13:12
 * Copyright (c) 2022, zaodao All Rights Reserved.
 */
public interface IProxyFactory {
    <T> T getProxy(final RpcReferenceWrapper rpcReferenceWrapper) throws Throwable;
}

package org.fatsnake.fatrpc.framework.core.proxy.jdk;

import org.fatsnake.fatrpc.framework.core.client.RpcReferenceWrapper;
import org.fatsnake.fatrpc.framework.core.proxy.IProxyFactory;

import java.lang.reflect.Proxy;

/**
 * @Auther: fatsnake
 * @Description":
 * @Date:2022/7/4 13:14
 * Copyright (c) 2022, zaodao All Rights Reserved.
 */
public class JDKProxyFactory implements IProxyFactory {
    @Override
    public <T> T getProxy(RpcReferenceWrapper rpcReferenceWrapper) throws Throwable {
        return (T) Proxy.newProxyInstance(rpcReferenceWrapper.getAimClass().getClassLoader(), new Class[]{rpcReferenceWrapper.getAimClass()},
                new JDKClientInvocationHandler(rpcReferenceWrapper));
    }
}

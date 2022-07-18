package org.fatsnake.fatrpc.framework.core.proxy.javassist;

import org.fatsnake.fatrpc.framework.core.client.RpcReferenceWrapper;
import org.fatsnake.fatrpc.framework.core.proxy.IProxyFactory;

/**
 * @Auther: fatsnake
 * @Description":
 * @Date:2022/7/4 9:08 下午
 * Copyright (c) 2022, zaodao All Rights Reserved.
 */
public class JavassistProxyFactory implements IProxyFactory {
    @Override
    public <T> T getProxy(RpcReferenceWrapper rpcReferenceWrapper) throws Throwable {
        return (T) ProxyGenerator.newProxyInstance(Thread.currentThread().getContextClassLoader(),
                rpcReferenceWrapper.getAimClass(), new JavassistInvocationHandler(rpcReferenceWrapper));
    }
}

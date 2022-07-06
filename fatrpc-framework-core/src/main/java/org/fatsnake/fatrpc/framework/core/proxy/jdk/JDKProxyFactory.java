package org.fatsnake.fatrpc.framework.core.proxy.jdk;

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
    public <T> T getProxy(Class clazz) throws Throwable {
        return (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class[]{clazz},
                new JDKClientInvocationHandler(clazz));
    }
}

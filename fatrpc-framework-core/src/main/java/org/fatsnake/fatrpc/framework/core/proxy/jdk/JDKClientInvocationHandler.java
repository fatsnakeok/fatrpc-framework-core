package org.fatsnake.fatrpc.framework.core.proxy.jdk;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @Auther: fatsnake
 * @Description":
 * @Date:2022/7/4 13:17
 * Copyright (c) 2022, zaodao All Rights Reserved.
 */
public class JDKClientInvocationHandler implements InvocationHandler {


    private final static Object OBJECT = new Object();

    private Class<?> clazz;

    public JDKClientInvocationHandler(Class<?> clazz) {
        this.clazz = clazz;
    }


    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        return null;
    }
}

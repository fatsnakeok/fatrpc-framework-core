package org.fatsnake.fatrpc.framework.core.proxy.javassist.demo;


import org.fatsnake.fatrpc.framework.core.server.DataService;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @Author fatsnake
 * @Date created in 8:44 上午 2021/12/9
 */
public class DemoInvocation implements InvocationHandler {


    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("this is invoke");
        return new Object();
    }

    public static void main(String[] args) throws Throwable {
        Method[] methods = DataService.class.getDeclaredMethods();
//        Demo$Proxy demo$Proxy = new Demo$Proxy(new DemoInvocation());
//        Demo$Proxy.methods = methods;
//        demo$Proxy.getList();
    }
}

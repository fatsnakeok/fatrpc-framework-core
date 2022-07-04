package org.fatsnake.fatrpc.framework.core.client;


import javassist.util.proxy.ProxyFactory;
import org.fatsnake.fatrpc.framework.core.proxy.IProxyFactory;

/**
 * @Author fatsnake
 * @Date created in 1I:49 上午 2021/12/11
 */
public class RpcReference {

    public IProxyFactory proxyFactory;

    public RpcReference(IProxyFactory proxyFactory) {
        this.proxyFactory = proxyFactory;
    }

    /**
     * 根据接口类型获取代理对象
     *
     * @param tClass
     * @param <T>
     * @return
     */
    public <T> T get(Class<T> tClass) throws Throwable {
        return proxyFactory.getProxy(tClass);
    }
}

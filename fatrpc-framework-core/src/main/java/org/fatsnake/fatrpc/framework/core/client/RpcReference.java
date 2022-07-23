package org.fatsnake.fatrpc.framework.core.client;


import io.netty.util.ReferenceCountUtil;
import javassist.util.proxy.ProxyFactory;
import org.fatsnake.fatrpc.framework.core.proxy.IProxyFactory;

import static org.fatsnake.fatrpc.framework.core.common.cache.CommonClientCache.CLIENT_CONFIG;

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
     * @param rpcReferenceWrapper
     * @param <T>
     * @return
     */
    public <T> T get(RpcReferenceWrapper<T>rpcReferenceWrapper) throws Throwable {
        initGlobalRpcReferenceWrapperConfig(rpcReferenceWrapper);
        return proxyFactory.getProxy(rpcReferenceWrapper);
    }

    private <T> void initGlobalRpcReferenceWrapperConfig(RpcReferenceWrapper<T> rpcReferenceWrapper) {
        if (rpcReferenceWrapper.getTimeOut() == null) {
            rpcReferenceWrapper.setTimeOut(CLIENT_CONFIG.getTimeOut());
        }
    }
}

package org.fatsnake.fatrpc.framework.core.common.event.listener;


import org.fatsnake.fatrpc.framework.core.common.event.FatRpcDestroyEvent;
import org.fatsnake.fatrpc.framework.core.registy.URL;

import static org.fatsnake.fatrpc.framework.core.common.cache.CommonServerCache.*;

/**
 * 服务注销 监听器
 *
 * @Author fatsnake
 * @Date created in 3:18 下午 2022/1/8
 */
public class ServiceDestroyListener implements FatRpcListener<FatRpcDestroyEvent> {

    @Override
    public void callBack(Object t) {
        for (URL url : PROVIDER_URL_SET) {
            REGISTRY_SERVICE.unRegister(url);
        }
    }
}

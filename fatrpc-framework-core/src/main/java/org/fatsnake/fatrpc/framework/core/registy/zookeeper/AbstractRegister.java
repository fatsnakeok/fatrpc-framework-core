package org.fatsnake.fatrpc.framework.core.registy.zookeeper;

import org.fatsnake.fatrpc.framework.core.registy.RegistryService;
import org.fatsnake.fatrpc.framework.core.registy.URL;

import java.util.List;
import java.util.Map;

import static org.fatsnake.fatrpc.framework.core.common.cache.CommonClientCache.SUBSCRIBE_SERVICE_LIST;
import static org.fatsnake.fatrpc.framework.core.common.cache.CommonServerCache.PROVIDER_URL_SET;

/**
 * @Auther: fatsnake
 * @Description": 对一些注册数据做统一的处理
 * 假设日后支持多种类型的注册中心，例如：redis、etcd等的话
 * 所有基础的记录操作都可以统一放在抽象类中实现。
 * @Date:2022/7/6 13:37
 * Copyright (c) 2022, zaodao All Rights Reserved.
 */
public abstract class AbstractRegister implements RegistryService {

    @Override
    public void  register(URL url) {
        PROVIDER_URL_SET.add(url);
    }

    @Override
    public void unRegister(URL url) {
        PROVIDER_URL_SET.remove(url);
    }

    @Override
    public void subscribe(URL url) {
        SUBSCRIBE_SERVICE_LIST.add(url);
    }

    @Override
    public void doUnSubscribe(URL url) {
        SUBSCRIBE_SERVICE_LIST.remove(url.getServiceName());
    }


    /**
     * 留给子类扩展
     * @param url
     */
    public abstract void doAfterSubscribe(URL url);

    /**
     * 留给子类扩展
     * @param url
     */
    public abstract void doBeforeSubscribe(URL url);


    /**
     * 留给子类扩展
     * @param serviceName
     * @return
     */
    public abstract List<String> getProviderIps(String serviceName);


    /**
     * 获取服务的权重信息
     * @param serviceName
     * @return
     */
    public abstract Map<String, String> getServiceWeightMap(String serviceName);

}

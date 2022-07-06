package org.fatsnake.fatrpc.framework.core.registy;

/**
 * @Auther: fatsnake
 * @Description": 远程服务信息的四个核心元操作，此处抽象出来，具体实现交给实现类，方便以后更换注册中心中间件比如换成 nacos
 * @Date:2022/7/6 10:57
 * Copyright (c) 2022, zaodao All Rights Reserved.
 */
public interface RegistryService {

    /**
     * 注册url/服务上线
     * <p>
     * 主要功能：
     * 将fatrpc服务信息写入注册中心
     * 当出现网络抖动的时候需要进行适当的重试做法
     * 注册服务url的时候需要写入持久化文件中
     *
     * 功能描述：
     * 注册接口，当某个服务要启动的时候，需要再将接口注册到注册中心，之后服务调用方才可以获取到新服务的数据了。
     *
     * @param url url
     */
    void register(URL url);

    /**
     * 服务下线
     * <p>
     * 主要功能：
     * 持久化节点是无法进行服务下线操作的
     * 下线的服务必须保证url是完整匹配的
     * 移除持久化文件的一些内容信息
     *
     * 功能描述：
     * 服务下线接口，当某个服务提供者要下线了，则需要主动将注册过的服务信息从zk的指定节点上摘除，此时就需要调用unRegister接口。
     *
     * @param url url
     */
    void unregister(URL url);

    /**
     * 消费方订阅服务
     *
     * 功能描述：
     * 订阅某个服务，通常是客户端在启动阶段需要调用的接口。客户端在启动过程中需要调用该函数，从注册中心中提取现有的服务提供者地址，从而实现服务订阅功能。
     *
     * @param url url
     */
    void subscribe(URL url);


    /**
     * 执行取消订阅内部的逻辑
     *
     * 功能描述：
     * 取消订阅服务，当服务调用方不打算再继续订阅某些服务的时候，就需要调用该函数去取消服务的订阅功能，将注册中心的订阅记录进行移除操作。
     *
     * @param url url
     */
    void doUnSubscribe(URL url);

}

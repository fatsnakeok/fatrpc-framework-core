package org.fatsnake.fatrpc.framework.core.common.cache;

import org.fatsnake.fatrpc.framework.core.common.ChannelFuturePollingRef;
import org.fatsnake.fatrpc.framework.core.common.ChannelFutureWrapper;
import org.fatsnake.fatrpc.framework.core.common.RpcInvocation;
import org.fatsnake.fatrpc.framework.core.common.config.ClientConfig;
import org.fatsnake.fatrpc.framework.core.registy.URL;
import org.fatsnake.fatrpc.framework.core.router.IRouter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Auther: fatsnake
 * @Description":
 * @Date:2022/7/3 3:57 下午
 * Copyright (c) 2022, zaodao All Rights Reserved.
 */
public class CommonClientCache {

    /**
     * 队列，用于实现异步发送信息操作
     */
    public static BlockingQueue<RpcInvocation> SEND_QUEUE = new ArrayBlockingQueue(100);
    /**
     * 用于存放响应结果
     */
    public static Map<String, Object> RESP_MAP = new ConcurrentHashMap<>();


    public static ClientConfig CLIENT_CONFIG;

    // provider名称 --> 该服务有哪些集群URL
    public static List<URL> SUBSCRIBE_SERVICE_LIST = new ArrayList<>();
    public static Map<String, List<URL>> URL_MAP = new ConcurrentHashMap<>();
    public static Set<String> SERVER_ADDRESS = new HashSet<>();
    // 每次进行远程调用的时候都是从这里去选择服务提供者
    public static Map<String, List<ChannelFutureWrapper>> CONNECT_MAP = new ConcurrentHashMap<>();

    // -- 路由层 -- start
    // 随机请求的map
    public static Map<String, ChannelFutureWrapper[]> SERVICE_ROUTER_MAP = new ConcurrentHashMap<>();
    public static ChannelFuturePollingRef CHANNEL_FUTURE_POLLING_REF = new ChannelFuturePollingRef();
    public static IRouter IROUTER;
    // -- 路由层 -- end
}

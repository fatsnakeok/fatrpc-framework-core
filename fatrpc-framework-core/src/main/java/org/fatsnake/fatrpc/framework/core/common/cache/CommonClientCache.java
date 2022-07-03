package org.fatsnake.fatrpc.framework.core.common.cache;

import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingDeque;
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
    public static BlockingDeque<RpcInvocation> SEND_QUEUE = new ArrayBlockingQueue<>(100);
    public static Map<String, Object> REST_MAP = new ConcurrentHashMap<>();
}

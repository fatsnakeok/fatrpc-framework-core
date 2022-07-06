package org.fatsnake.fatrpc.framework.core.common.cache;

import org.fatsnake.fatrpc.framework.core.common.RpcInvocation;

import java.util.Map;
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
}

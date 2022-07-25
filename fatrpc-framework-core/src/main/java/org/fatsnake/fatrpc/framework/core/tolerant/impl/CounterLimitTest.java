package org.fatsnake.fatrpc.framework.core.tolerant.impl;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * @Auther: fatsnake
 * @Description":
 * @Date:2022/7/24 1:52 下午
 * Copyright (c) 2022, zaodao All Rights Reserved.
 */
public class CounterLimitTest {

    private static FixedWindowCounterLimit fixedWindowCounterLimit = new FixedWindowCounterLimit(10, 60, TimeUnit.SECONDS);

    private static SlidingWindowCounterLimit slidingWindowCounterLimit = new SlidingWindowCounterLimit(20, 10, 10);

    public static void main(String[] args) {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        for (int i=0; i<100; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        countDownLatch.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (!slidingWindowCounterLimit.tryCount()) {
                        return;
                    }
                    System.out.println("执行核心业务！ ");
                }
            }).start();
        }
        countDownLatch.countDown();
        System.out.println("启动并发测试");
        Thread.yield();
    }
}

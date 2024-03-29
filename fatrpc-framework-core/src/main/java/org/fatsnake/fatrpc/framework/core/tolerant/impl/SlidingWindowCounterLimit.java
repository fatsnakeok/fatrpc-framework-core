package org.fatsnake.fatrpc.framework.core.tolerant.impl;

import org.fatsnake.fatrpc.framework.core.tolerant.CounterLimit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Auther: fatsnake
 * @Description":
 * @Date:2022/7/24 1:53 下午
 * Copyright (c) 2022, zaodao All Rights Reserved.
 */
public class SlidingWindowCounterLimit extends CounterLimit {

    private static Logger logger = LoggerFactory.getLogger(SlidingWindowCounterLimit.class);

    private AtomicInteger[] gridDistribution;

    private volatile  int currentIndex = 0;

    /**
     * 当前时间之前的滑动窗口计数
     */
    private int preTotalCount;

    /**
     * 格子数
     */
    private int gridNumber;

    /**
     * 是否正在执行状态重置
     */
    private volatile boolean resetting;

    public SlidingWindowCounterLimit(int gridNumber, int limitCount, int limitTime) {
        this(gridNumber, limitCount, limitTime, TimeUnit.SECONDS);
    }

    public SlidingWindowCounterLimit(int gridNumber, int limitCount, int limitTime, TimeUnit seconds) {
        if (gridNumber <= limitTime)
            throw new RuntimeException("无法完成限流，gridNumber必须大于limitTime，gridNumber = " + gridNumber + ",limitTime = " + limitTime);
        this.gridNumber = gridNumber;
        this.limitCount = limitCount;
        this.limitTime = limitTime;
        this.timeUnit = timeUnit;
        gridDistribution = new AtomicInteger[gridNumber];
        for (int i = 0; i < gridNumber; i++) {
            gridDistribution[i] = new AtomicInteger(0);
        }
        new Thread(new CounterResetThread()).start();
    }

    @Override
    protected boolean tryCount() {
        while (true) {
            if (isLimited) {
                return false;
            } else {
                int currentGridCount = gridDistribution[currentIndex].get();
                if (preTotalCount + currentGridCount == limitCount) {
                    logger.info("限流：{}", LocalDateTime.now().toString());
                    isLimited = true;
                    return false;
                }
                if (!resetting && gridDistribution[currentIndex].compareAndSet(currentGridCount, currentGridCount + 1)) {
                    return true;
                }
            }
        }
    }

    class CounterResetThread implements Runnable {

        @Override
        public void run() {
            while (true) {
                try {
                    timeUnit.sleep(1);
                    //todo ?
                    int indexToReset = currentIndex - limitCount - 1;
                    if (indexToReset < 0) {
                        indexToReset += gridNumber;
                    }
                    resetting = true;
                    //重置当前时间窗口之前的滑动窗口计数器
                    preTotalCount = preTotalCount - gridDistribution[indexToReset].get() + gridDistribution[currentIndex++].get();
                    if (currentIndex == gridNumber) currentIndex = 0;
                    if(preTotalCount + gridDistribution[currentIndex].get() < limitCount) {
                        isLimited = false;
                    }
                    resetting = false;
                    logger.info("当前格子：{}，重置格子：{}，重置格子访问量：{}，前窗口格子总数：{}",
                            currentIndex, indexToReset, gridDistribution[indexToReset].get(), preTotalCount);
                    gridDistribution[indexToReset].set(0);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

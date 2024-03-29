package org.fatsnake.fatrpc.framework.core.tolerant.impl;

import org.fatsnake.fatrpc.framework.core.tolerant.CounterLimit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Time;
import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Auther: fatsnake
 * @Description":
 * @Date:2022/7/24 1:53 下午
 * Copyright (c) 2022, zaodao All Rights Reserved.
 */
public class FixedWindowCounterLimit extends CounterLimit {

    private static Logger logger = LoggerFactory.getLogger(FixedWindowCounterLimit.class);

    private AtomicInteger counter = new AtomicInteger(0);

    public FixedWindowCounterLimit(int limitCount, long limitTime) {
        this(limitCount, limitTime, TimeUnit.SECONDS);
    }

    public FixedWindowCounterLimit(int limitCount, long limitTime, TimeUnit timeUnit) {

        this.limitCount = limitCount;
        this.limitTime = limitTime;
        this.timeUnit = timeUnit;
        // 开启计数器清零线程
        new Thread(new CounterResetThread()).start();
    }

    @Override
    protected boolean tryCount() {
        while (true) {
            if (isLimited) {
                return false;
            }
            int currentCount = counter.get();
            if (currentCount == limitCount) {
                logger.info("限制流量：{}", LocalDateTime.now().toString());
                isLimited = true;
                return false;
            }
            if (counter.compareAndSet(currentCount, currentCount + 1)){
                return true;
            }
        }
    }

    class CounterResetThread implements Runnable {
        @Override
        public void run() {
            try {
                timeUnit.sleep(limitTime);
                // 计数器清空
                counter.compareAndSet(limitCount, 0);
                isLimited = false;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

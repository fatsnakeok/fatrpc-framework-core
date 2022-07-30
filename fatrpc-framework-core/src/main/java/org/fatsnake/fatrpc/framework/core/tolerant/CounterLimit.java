package org.fatsnake.fatrpc.framework.core.tolerant;

import java.time.temporal.ValueRange;
import java.util.concurrent.TimeUnit;

/**
 * @Auther: fatsnake
 * @Description":
 * @Date:2022/7/24 2:00 下午
 * Copyright (c) 2022, zaodao All Rights Reserved.
 */
public abstract class CounterLimit {
    protected int limitCount;

    protected long limitTime;

    protected TimeUnit timeUnit;

    protected volatile boolean isLimited;

    protected abstract boolean tryCount();
}

package org.fatsnake.fatrpc.framework.core.common.event.listener;

/**
 * @Auther: fatsnake
 * @Description":
 * @Date:2022/7/12 11:04
 * Copyright (c) 2022, zaodao All Rights Reserved.
 */
public interface IRpcListener<T> {
    void callBack(Object t);
}

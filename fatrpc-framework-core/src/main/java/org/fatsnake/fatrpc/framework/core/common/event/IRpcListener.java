package org.fatsnake.fatrpc.framework.core.common.event;

/**
 * @Auther: fatsnake
 * @Description": 监听器接口
 * @Date:2022/7/8 13:52
 * Copyright (c) 2022, zaodao All Rights Reserved.
 */
public interface IRpcListener<T> {
    void callBack(Object t);
}

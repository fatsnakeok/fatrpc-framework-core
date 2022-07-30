package org.fatsnake.fatrpc.framework.core.common.event;

/**
 * @Auther: fatsnake
 * @Description": 定义抽象事件，用于装载需要传递的数据信息
 * @Date:2022/7/8 13:50
 * Copyright (c) 2022, zaodao All Rights Reserved.
 */
public interface FatRpcEvent {

    Object getData();

    FatRpcEvent setData(Object data);
}

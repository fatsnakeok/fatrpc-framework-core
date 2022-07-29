package org.fatsnake.fatrpc.framework.core.common.event;

/**
 * @Auther: fatsnake
 * @Description": 节点更新事件
 * @Date:2022/7/8 10:12 下午
 * Copyright (c) 2022, zaodao All Rights Reserved.
 */
public class FatRpcUpdateEvent implements FatRpcEvent {

    private Object data;

    public FatRpcUpdateEvent(Object data) {
        this.data = data;
    }

    @Override
    public Object getData() {
        return data;
    }

    @Override
    public FatRpcEvent setData(Object data) {
        this.data = data;
        return this;
    }
}

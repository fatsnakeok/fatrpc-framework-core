package org.fatsnake.fatrpc.framework.core.common.event;

/**
 * @Auther: fatsnake
 * @Description": 节点更新事件
 * @Date:2022/7/8 10:12 下午
 * Copyright (c) 2022, zaodao All Rights Reserved.
 */
public class IRpcUpdateEvent implements IRpcEvent{

    private Object data;

    public IRpcUpdateEvent(Object data) {
        this.data = data;
    }

    @Override
    public Object getData() {
        return data;
    }

    @Override
    public IRpcEvent setData(Object data) {
        this.data = data;
        return this;
    }
}

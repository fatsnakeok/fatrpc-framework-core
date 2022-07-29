package org.fatsnake.fatrpc.framework.core.common.event;

/**
 * @Auther: fatsnake
 * @Description":
 * @Date:2022/7/12 6:51 上午
 * Copyright (c) 2022, zaodao All Rights Reserved.
 */
public class FatRpcNodeChangeEvent implements FatRpcEvent {

    private Object data;

    public FatRpcNodeChangeEvent(Object data) {
        this.data = data;
    }

    public Object getData() {
        return data;
    }

    @Override
    public FatRpcEvent setData(Object data) {
        this.data = data;
        return this;
    }
}

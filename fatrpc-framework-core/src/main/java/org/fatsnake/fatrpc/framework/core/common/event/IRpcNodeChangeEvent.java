package org.fatsnake.fatrpc.framework.core.common.event;

/**
 * @Auther: fatsnake
 * @Description":
 * @Date:2022/7/12 6:51 上午
 * Copyright (c) 2022, zaodao All Rights Reserved.
 */
public class IRpcNodeChangeEvent implements IRpcEvent{

    private Object data;

    public IRpcNodeChangeEvent(Object data) {
        this.data = data;
    }

    public Object getData() {
        return data;
    }

    @Override
    public IRpcEvent setData(Object data) {
        this.data = data;
        return this;
    }
}

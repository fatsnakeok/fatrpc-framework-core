package org.fatsnake.fatrpc.framework.core.common.event;

/**
 * 服务销毁事件
 *
 * @Author fatsnake
 * @Date created in 3:20 下午 2022/1/8
 */
public class FatRpcDestroyEvent implements FatRpcEvent {

    private Object data;

    public FatRpcDestroyEvent(Object data) {
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

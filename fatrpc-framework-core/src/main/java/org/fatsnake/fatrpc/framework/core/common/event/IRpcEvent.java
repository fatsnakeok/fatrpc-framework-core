package org.fatsnake.fatrpc.framework.core.common.event;

/**
 * @Auther: fatsnake
 * @Description":
 * @Date:2022/7/8 13:50
 * Copyright (c) 2022, zaodao All Rights Reserved.
 */
public interface IRpcEvent {

    Object getData();

    IRpcEvent setData(Object data);
}

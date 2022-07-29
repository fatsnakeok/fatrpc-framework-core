package org.fatsnake.fatrpc.framework.interfaces;

import java.util.List;

/**
 * @Auther: fatsnake
 * @Description":
 * @Date:2022/7/29 11:21
 * Copyright (c) 2022, zaodao All Rights Reserved.
 */
public interface OrderService {
    List<String> getOrderNoList();

    String testMaxData(int i);
}

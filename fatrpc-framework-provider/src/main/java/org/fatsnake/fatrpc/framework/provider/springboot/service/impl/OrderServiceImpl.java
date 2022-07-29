package org.fatsnake.fatrpc.framework.provider.springboot.service.impl;

import org.fatsnake.fatrpc.framework.interfaces.OrderService;
import org.fatsnake.fatrpc.framework.spring.starter.common.FatRpcService;

import java.util.Arrays;
import java.util.List;

/**
 * @Auther: fatsnake
 * @Description":
 * @Date:2022/7/29 13:18
 * Copyright (c) 2022, zaodao All Rights Reserved.
 */
@FatRpcService(serviceToken = "order-token", group = "order-group", limit = 2)
public class OrderServiceImpl implements OrderService {
    @Override
    public List<String> getOrderNoList() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return Arrays.asList("item1", "item2");
    }

    // 测试大数据包传输是否有异常
    @Override
    public String testMaxData(int i) {
        StringBuilder stb = new StringBuilder();
        for (int j=0 ;j < i;j++) {
            stb.append("1");
        }
        return stb.toString();
    }
}

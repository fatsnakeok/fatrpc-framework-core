package org.fatsnake.fatrpc.framework.core.server;

import org.fatsnake.fatrpc.framework.interfaces.DataService;

import java.util.ArrayList;
import java.util.List;

/**
 * @Auther: fatsnake
 * @Description":
 * @Date:2022/7/3 3:33 下午
 * Copyright (c) 2022, zaodao All Rights Reserved.
 */
public class DataServiceImpl implements DataService {
    @Override
    public String sendData(String body) {
        System.out.println("begin");
//            try {
//                Thread.sleep(10000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
        System.out.println("这里是服务提供者，body is：" + body);
        return "success";
    }

    @Override
    public List<String> getList() {
        ArrayList arrayList = new ArrayList();
        arrayList.add("fat1");
        arrayList.add("fat2");
        arrayList.add("fat3");
        return arrayList;
    }

    @Override
    public void testError() {
        System.out.println(1/0);
    }

    @Override
    public String testErrorV2() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("调用测试");
        System.out.println(1/0);
        return "error";
    }
}

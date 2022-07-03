package org.fatsnake.fatrpc.framework.core.server;

import org.fatsnake.fatrpc.framework.interfaces.IDataService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @Auther: fatsnake
 * @Description":
 * @Date:2022/7/3 3:33 下午
 * Copyright (c) 2022, zaodao All Rights Reserved.
 */
public class DataService  implements IDataService {
    @Override
    public String sendData(String body) {
        System.out.println("已收到的参数长度：" + body.length());
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
}

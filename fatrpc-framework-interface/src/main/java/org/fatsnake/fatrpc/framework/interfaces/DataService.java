package org.fatsnake.fatrpc.framework.interfaces;

import java.util.List;

/**
 * @Auther: fatsnake
 * @Description":  定义测试类接口
 * @Date:2022/7/3 3:11 下午
 * Copyright (c) 2022, zaodao All Rights Reserved.
 */
public interface DataService {

    /**
     * 发送数据
     * @param body body
     * @return String
     */
    String sendData(String body);


    /**
     * 获取数据
     *
     * @return
     */
    List<String> getList();

}

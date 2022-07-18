package org.fatsnake.fatrpc.framework.core.spi.jdk;

/**
 * @Auther: fatsnake
 * @Description":
 * @Date:2022/7/18 14:24
 * Copyright (c) 2022, zaodao All Rights Reserved.
 */
public class DefaultISpiTest implements ISpiTest{
    @Override
    public void doTest() {
        System.out.println("执行测试方法");
    }
}

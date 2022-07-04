package org.fatsnake.fatrpc.framework.core.proxy.javassist.demo;

import java.util.ArrayList;
import java.util.List;

/**
 * @Auther: fatsnake
 * @Description":
 * @Date:2022/7/4 7:53 下午
 * Copyright (c) 2022, zaodao All Rights Reserved.
 */
public class Demo {

    public void doTest() {System.out.println("this is demo");}

    public String findStr() {return "success";}

    public List<String> findList() {
        return new ArrayList<>();
    }
}

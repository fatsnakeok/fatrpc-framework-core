package org.fatsnake.fatrpc.framework.core.spi.jdk;

import java.util.Iterator;
import java.util.ServiceLoader;

/**
 * @Auther: fatsnake
 * @Description":
 * @Date:2022/7/18 14:23
 * Copyright (c) 2022, zaodao All Rights Reserved.
 */
public class TestSpiDemo {


    public static void main(String[] args) {
        ServiceLoader<ISpiTest> serviceLoader = ServiceLoader.load(ISpiTest.class);
        Iterator<ISpiTest>iSpiTestIterator = serviceLoader.iterator();
        while (iSpiTestIterator.hasNext()) {
            ISpiTest spiTest = iSpiTestIterator.next();
            TestSpiDemo.doTest(spiTest);
        }
    }

    private static void doTest(ISpiTest spiTest) {
        System.out.println("begin");
        spiTest.doTest();
        System.out.println("end");
    }

}

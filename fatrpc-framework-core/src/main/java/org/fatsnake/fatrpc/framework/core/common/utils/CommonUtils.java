package org.fatsnake.fatrpc.framework.core.common.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @Auther: fatsnake
 * @Description":
 * @Date:2022/7/3 4:06 下午
 * Copyright (c) 2022, zaodao All Rights Reserved.
 */
public class CommonUtils {

    public static List<Class<?>> getAllInterfaces(Class targetClass) {
        if (targetClass == null) {
            throw  new IllegalArgumentException("targetClass is null!");
        }
        Class[] clazz = targetClass.getInterfaces();
        if (clazz.length == 0) {
            return Collections.emptyList();
        }
        List<Class<?>> classes = new ArrayList<>(clazz.length);
        for (Class aClass : clazz) {
            classes.add(aClass);
        }
        return classes;
    }

}

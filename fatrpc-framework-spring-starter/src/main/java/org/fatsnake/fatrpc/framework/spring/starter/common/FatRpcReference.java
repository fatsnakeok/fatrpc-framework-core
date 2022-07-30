package org.fatsnake.fatrpc.framework.spring.starter.common;


import java.lang.annotation.*;


/**
 * @Auther: fatsnake
 * @Description":
 * @Date:2022/7/27 16:43
 * Copyright (c) 2022, zaodao All Rights Reserved.
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)  // RetentionPolicy.RUNTIME 注解不仅被保存到class文件中，jvm加载class文件之后，仍然存在
@Documented
public @interface FatRpcReference {
    String url() default "";

    String group() default "default";

    String serviceToken() default "";

    int timeOut() default 3000;

    int retry() default 1;

    boolean async() default false;
}

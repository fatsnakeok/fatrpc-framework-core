package org.fatsnake.fatrpc.framework.spring.starter.common;

import org.springframework.stereotype.Component;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Auther: fatsnake
 * @Description":
 * @Date:2022/7/28 6:21 上午
 * Copyright (c) 2022, zaodao All Rights Reserved.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component  // 这就是，为什么携带了 @ FatRpcService注解的bean会被扫描到Spring容器中
public @interface FatRpcService {

    int limit() default 0;

    String group() default "default";

    String  serviceToken() default "";
}

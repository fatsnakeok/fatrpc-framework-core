package org.fatsnake.fatrpc.framework.spring.starter.config;

import org.fatsnake.fatrpc.framework.core.common.event.IRpcListenerLoader;
import org.fatsnake.fatrpc.framework.core.server.ApplicationShutdownHook;
import org.fatsnake.fatrpc.framework.core.server.Server;
import org.fatsnake.fatrpc.framework.core.server.ServiceWrapper;
import org.fatsnake.fatrpc.framework.spring.starter.common.FatRpcService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.Map;

/**
 * @Auther: fatsnake
 * @Description":
 * @Date:2022/7/28 16:48
 * Copyright (c) 2022, zaodao All Rights Reserved.
 *
 *  ApplicationContextAware:
 * 当一个类实现了这个接口之后，这个类就可以方便的获得ApplicationContext对象（spring上下文），Spring发现某个Bean实现了ApplicationContextAware接口，
 * Spring容器会在创建该Bean之后，自动调用该Bean的setApplicationContext（参数）方法，调用该方法时，会将容器本身ApplicationContext对象作为参数传递
 * 给该方法。
 *
 *  InitializingBean:
 *  当一个类实现这个接口之后，Spring启动后，初始化Bean时，若该Bean实现InitialzingBean接口，会自动调用afterPropertiesSet()方法，完成一些用户自定义的初始化操作。
 */
public class FatRpcServerAutoConfiguration implements InitializingBean, ApplicationContextAware {

    private static final Logger LOGGER = LoggerFactory.getLogger(FatRpcServerAutoConfiguration.class);

    private ApplicationContext applicationContext;



    @Override
    public void afterPropertiesSet() throws Exception {
        Server server = null;
        Map<String, Object> beanMap = applicationContext.getBeansWithAnnotation(FatRpcService.class);
        if (beanMap.size() == 0) {
            // 说明当前应用不需要对外提供服务
            return;
        }
        printBanner();
        long begin = System.currentTimeMillis();
        server = new Server();
        server.initServerConfig();
        IRpcListenerLoader iRpcListenerLoader = new IRpcListenerLoader();
        iRpcListenerLoader.init();
        for (String beanName : beanMap.keySet()) {
            Object bean = beanMap.get(beanName);
            FatRpcService fatRpcService = bean.getClass().getAnnotation(FatRpcService.class);
            ServiceWrapper dataServiceServiceWrapper = new ServiceWrapper(bean, fatRpcService.group());
            dataServiceServiceWrapper.setServiceToken(fatRpcService.serviceToken());
            dataServiceServiceWrapper.setLimit(fatRpcService.limit());
            server.exportService(dataServiceServiceWrapper);
            LOGGER.info(">>>>>>>>>>>>>>> [irpc] {} export success! >>>>>>>>>>>>>>> ",beanName);
        }
        long end = System.currentTimeMillis();
        ApplicationShutdownHook.registryShutdownHook();
        server.startApplication();
        LOGGER.info(" ================== [{}] started success in {}s ================== ",server.getServerConfig().getApplicationName(),((double)end-(double)begin)/1000);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }


    private void printBanner(){
        System.out.println();
        System.out.println("==============================================");
        System.out.println("|||---------- IRpc Starting Now! ----------|||");
        System.out.println("==============================================");
        System.out.println("源代码地址: https://github.com/fatsnakeok/fatrpc-framework-core");
        System.out.println("version: 1.0.0");
        System.out.println();
    }
}

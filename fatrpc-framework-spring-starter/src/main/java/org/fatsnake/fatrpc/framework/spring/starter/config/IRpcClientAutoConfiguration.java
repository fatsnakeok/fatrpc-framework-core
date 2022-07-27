package org.fatsnake.fatrpc.framework.spring.starter.config;

import org.fatsnake.fatrpc.framework.core.client.Client;
import org.fatsnake.fatrpc.framework.core.client.ConnectionHandler;
import org.fatsnake.fatrpc.framework.core.client.RpcReference;
import org.fatsnake.fatrpc.framework.core.client.RpcReferenceWrapper;
import org.fatsnake.fatrpc.framework.spring.starter.common.IRpcReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import sun.font.TrueTypeGlyphMapper;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;

/**
 * @Auther: fatsnake
 * @Description":
 *
 * BeanPostProcessor: 该接口我们也叫后置处理器，作用是在Bean对象在实例化和依赖注入完毕后，
 * 在显示调用初始化方法的前后添加我们自己的逻辑。注意是Bean实例化完毕后及依赖注入完成后触发的
 *
 * 这个类主要是负责在每个bean启动的时候，对bean里面凡是携带有IRpcReference注解的字段都修改其引用，使其指向对应的代理对象
 *
 * @Date:2022/7/28 6:29 上午
 * Copyright (c) 2022, zaodao All Rights Reserved.
 */
public class IRpcClientAutoConfiguration implements BeanPostProcessor, ApplicationListener<ApplicationEvent> {


    private static RpcReference rpcReference = null;
    private static Client client = null;
    private volatile boolean needIninClient = false;
    private volatile boolean hasInitClientConfig = false;

    private static final Logger LOGGER = LoggerFactory.getLogger(IRpcClientAutoConfiguration.class);


    /**
     * 实例化、依赖注入完毕，
     * 在调用显示的初始化之前完成一些定制的初始化任务
     * @param bean
     * @param beanName
     * @return
     * @throws BeansException
     */
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        Field[] fields = bean.getClass().getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(IRpcReference.class)) {
                if (!hasInitClientConfig) {
                    client = new Client();
                    try {
                        rpcReference = client.initClientApplication();
                    } catch (Exception e) {
                        LOGGER.error("[IRpcClientAutoConfiguration] postProcessAfterInitialization has error ", e);
                        e.printStackTrace();
                    }
                    hasInitClientConfig = true;
                }
                needIninClient = true;
                IRpcReference iRpcReference = field.getAnnotation(IRpcReference.class);

                try {
                    field.setAccessible(true);
                    Object refObj = field.get(bean);
                    RpcReferenceWrapper rpcReferenceWrapper = new RpcReferenceWrapper();
                    rpcReferenceWrapper.setAimClass(field.getClass());
                    rpcReferenceWrapper.setGroup(iRpcReference.group());
                    rpcReferenceWrapper.setServiceToken(iRpcReference.serviceToken());
                    rpcReferenceWrapper.setUrl(iRpcReference.url());
                    // 失败重试次数
                    rpcReferenceWrapper.setRetry(iRpcReference.retry());
                    rpcReferenceWrapper.setAsync(iRpcReference.async());
                    refObj = rpcReference.get(rpcReferenceWrapper);
                    field.set(bean, refObj);
                    client.doSubscribeService(field.getType());
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
            }
        }
        return bean;
    }

    /**
     * 需要执行的逻辑代码，当spring容器初始化完成后就会执行该方法。
     * 启动客户端netty
     * @param applicationEvent
     */
    @Override
    public void onApplicationEvent(ApplicationEvent applicationEvent) {
        if (needIninClient && client != null) {
            LOGGER.info(" ================== [{}] started success ================== ",client.getClientConfig().getApplicationName());
            ConnectionHandler.setBootstrap(client.getBootstrap());
            client.doConnectServer();
            client.startClient();
        }
    }
}

package org.fatsnake.fatrpc.framework.core.common.event;


import org.fatsnake.fatrpc.framework.core.common.event.listener.ServiceUpdateListener;
import org.fatsnake.fatrpc.framework.core.common.utils.CommonUtils;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Auther: fatsnake
 * @Description": 发送事件操作实现
 * @Date:2022/7/8 13:52
 * Copyright (c) 2022, zaodao All Rights Reserved.
 */
public class FatRpcListenerLoader {

    private static List<FatRpcListener> fatRpcListenerList = new ArrayList<>();

    private static ExecutorService eventThreadPool = Executors.newFixedThreadPool(2);

    public static void registerListener(FatRpcListener fatRpcListener) {
        fatRpcListenerList.add(fatRpcListener);
    }

    public static void sendSyncEvent(FatRpcEvent fatRpcEvent) {
        System.out.println(fatRpcListenerList);
        if (CommonUtils.isEmptyList(fatRpcListenerList)) {
            return;
        }
        for (FatRpcListener<?> fatRpcListener : fatRpcListenerList) {
            Class<?> type = getInterfaceT(fatRpcListener);
            if (type.equals(fatRpcEvent.getClass())) {
                try {
                    fatRpcListener.callBack(fatRpcEvent.getData());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void init() {
        registerListener(new ServiceUpdateListener());
    }

    /**
     * 获取接口上的泛型T
     *
     * @param o
     * @return
     */
    public static Class<?> getInterfaceT(Object o) {
        Type[] types = o.getClass().getGenericInterfaces();
        ParameterizedType parameterizedType = (ParameterizedType) types[0];
        Type type = parameterizedType.getActualTypeArguments()[0];
        if (type instanceof Class<?>) {
            return (Class<?>) type;
        }
        return null;
    }

    public static void sendEvent(FatRpcEvent fatRpcEvent) {
        if (CommonUtils.isEmptyList(fatRpcListenerList)) {
            return;
        }

        for (FatRpcListener<?> fatRpcListener : fatRpcListenerList) {
            Class<?> type = getInterfaceT(fatRpcListener);
            if (type.equals(fatRpcEvent.getClass())) {
                eventThreadPool.execute(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            fatRpcListener.callBack(fatRpcEvent.getData());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }


        }


    }


}

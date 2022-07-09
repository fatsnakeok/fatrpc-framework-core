package org.fatsnake.fatrpc.framework.core.common.event;


import org.fatsnake.fatrpc.framework.core.common.RpcInvocation;
import org.fatsnake.fatrpc.framework.core.common.event.listener.ServiceUpdateListener;
import org.fatsnake.fatrpc.framework.core.common.utils.CommonUtils;
import org.jboss.netty.handler.ipfilter.IpFilterRuleList;

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
public class IRpcListenerLoader {

    private static List<IRpcListener> iRpcListenerList = new ArrayList<>();

    private static ExecutorService eventThreadPool = Executors.newFixedThreadPool(2);

    public static void registerListener(IRpcListener iRpcListener) {
        iRpcListenerList.add(iRpcListener);
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

    public static void sendEvent(IRpcEvent iRpcEvent) {
        if (CommonUtils.isEmptyList(iRpcListenerList)) {
            return;
        }

        for (IRpcListener<?> iRpcListener : iRpcListenerList) {
            Class<?> type = getInterfaceT(iRpcListener);
            if (type.equals(iRpcEvent.getClass())) {
                eventThreadPool.execute(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            iRpcListener.callBack(iRpcEvent.getData());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }


        }


    }


}

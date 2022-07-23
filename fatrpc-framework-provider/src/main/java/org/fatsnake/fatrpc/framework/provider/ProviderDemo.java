package org.fatsnake.fatrpc.framework.provider;


import org.fatsnake.fatrpc.framework.core.common.event.IRpcListenerLoader;
import org.fatsnake.fatrpc.framework.core.server.ApplicationShutdownHook;
import org.fatsnake.fatrpc.framework.core.server.DataServiceImpl;
import org.fatsnake.fatrpc.framework.core.server.Server;
import org.fatsnake.fatrpc.framework.core.server.ServiceWrapper;
import org.fatsnake.fatrpc.framework.core.server.UserServiceImpl;

import java.io.IOException;

/**
 * @Auther: fatsnake
 * @Description":
 * @Date:2022/7/19 4:44 下午
 * Copyright (c) 2022, zaodao All Rights Reserved.
 */
public class ProviderDemo {

    public static void main(String[] args) throws IOException, ClassNotFoundException, InstantiationException, IllegalAccessException, InterruptedException {
        Server server = new Server();
        server.initServerConfig();
        IRpcListenerLoader iRpcListenerLoader = new IRpcListenerLoader();
        iRpcListenerLoader.init();
        ServiceWrapper dataServiceServiceWrapper = new ServiceWrapper(new DataServiceImpl(), "dev");
        dataServiceServiceWrapper.setServiceToken("token-a");
        dataServiceServiceWrapper.setLimit(2);
        ServiceWrapper userServiceServiceWrapper = new ServiceWrapper(new UserServiceImpl(), "dev");
        userServiceServiceWrapper.setServiceToken("token-b");
        userServiceServiceWrapper.setLimit(2);
        server.exportService(dataServiceServiceWrapper);
        server.exportService(userServiceServiceWrapper);
        ApplicationShutdownHook.registryShutdownHook();
        server.startApplication();
    }

}

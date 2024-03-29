package org.fatsnake.fatrpc.framework.core.server;


import org.fatsnake.fatrpc.framework.core.common.event.FatRpcDestroyEvent;
import org.fatsnake.fatrpc.framework.core.common.event.FatRpcListenerLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 监听java进程被关闭
 *
 * @Author linhao
 * @Date created in 9:11 下午 2021/12/19
 */
public class ApplicationShutdownHook {

    public static Logger LOGGER = LoggerFactory.getLogger(ApplicationShutdownHook.class);

    /**
     * 注册一个shutdownHook的钩子，当jvm进程关闭的时候触发
     */
    public static void registryShutdownHook(){
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            @Override
            public void run() {
                LOGGER.info("[registryShutdownHook] ==== ");
                FatRpcListenerLoader.sendSyncEvent(new FatRpcDestroyEvent("destroy"));
                System.out.println("destory");
            }
        }));
    }

}

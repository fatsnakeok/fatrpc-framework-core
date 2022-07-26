package org.fatsnske.fatrpc.framework.consumer;

import org.fatsnake.fatrpc.framework.core.client.Client;
import org.fatsnake.fatrpc.framework.core.client.ConnectionHandler;
import org.fatsnake.fatrpc.framework.core.client.RpcReference;
import org.fatsnake.fatrpc.framework.core.client.RpcReferenceFuture;
import org.fatsnake.fatrpc.framework.core.client.RpcReferenceWrapper;
import org.fatsnake.fatrpc.framework.interfaces.DataService;

import java.util.List;
import java.util.Scanner;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;


/**
 * @Author linhao
 * @Date created in 4:25 下午 2022/2/4
 */
public class ConsumerDemo {

    public static void doAsyncRef() {
        RpcReferenceFuture rpcReferenceFuture = new RpcReferenceFuture<>();

    }

    public static void main(String[] args) throws Throwable {
        Client client = new Client();
        RpcReference rpcReference = client.initClientApplication();
        RpcReferenceWrapper<DataService> rpcReferenceWrapper = new RpcReferenceWrapper<>();
        rpcReferenceWrapper.setAimClass(DataService.class);
        rpcReferenceWrapper.setGroup("dev");
        rpcReferenceWrapper.setServiceToken("token-a");
        rpcReferenceWrapper.setTimeOut(3000);
        //失败重试次数
        rpcReferenceWrapper.setRetry(0);
        // 如果要使用future，这里是要切换为fasle
        rpcReferenceWrapper.setAsync(true);
        DataService dataService = rpcReference.get(rpcReferenceWrapper);
        //订阅服务
        client.doSubscribeService(DataService.class);

        ConnectionHandler.setBootstrap(client.getBootstrap());
        client.doConnectServer();
        client.startClient();
        while (true) {
            Scanner scanner = new Scanner(System.in);
            String input = scanner.nextLine();
            for (int i = 0; i < 4; i++) {
                String result = dataService.testErrorV2();
                System.out.println(result);
            }
            System.out.println("结束调用");
        }
    }
}

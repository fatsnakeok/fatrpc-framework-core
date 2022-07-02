package org.fatsnake.fatrpc.framework.core.bio;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Auther: fatsnake
 * @Description": 于BIO实现的阻塞IO服务端
 * @Date:2022/7/2 11:29 上午
 * Copyright (c) 2022, zaodao All Rights Reserved.
 */
public class BioServer {

    private static ExecutorService executorService = Executors.newFixedThreadPool(10);

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket();
        serverSocket.bind(new InetSocketAddress(1009));
        while (true) {
            // 堵塞状态 --1
            Socket socket = serverSocket.accept();
            System.out.println("获取新连接");
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    while (true) {
                        InputStream inputStream = null;
                        try {
                            // 堵塞的状态点--2
                            inputStream = socket.getInputStream();
                            byte[] result = new byte[1024];
                            int len = inputStream.read(result);
                            if (len != -1) {
                                System.out.println("[response]" + new String(result, 0, len));
                                OutputStream outputStream = socket.getOutputStream();
                                outputStream.write("respone data".getBytes(StandardCharsets.UTF_8));
                                outputStream.flush();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        }
    }
}

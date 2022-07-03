package org.fatsnake.fatrpc.framework.core.nio;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * @author linhao
 * @date created in 10:39 上午 2020/10/8
 */
public class NioSocketClient {

    public static void main(String[] args) {
        SocketChannel socketChannel = null;
        try {
            socketChannel = SocketChannel.open();
            socketChannel.connect(new InetSocketAddress(InetAddress.getLocalHost(), 8888));
            socketChannel.configureBlocking(false);
            while (true) {
                socketChannel.write(ByteBuffer.wrap(("this is test " + Thread.currentThread().getName()).getBytes()));
                Thread.sleep(2000);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
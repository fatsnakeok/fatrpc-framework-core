package org.fatsnake.fatrpc.framework.core.common;

import java.io.Serializable;
import java.util.Arrays;

import static org.fatsnake.fatrpc.framework.core.common.constans.RpcConstants.MAGIC_NUMBER;

/**
 * @Auther: fatsnake
 * @Description": 自定义协议
 * @Date:2022/7/4 13:34
 * Copyright (c) 2022, zaodao All Rights Reserved.
 */
public class RpcProtocol implements Serializable {
    private static final long serialVersionUID = 5359096060555795690L;

    /**
     * 魔法数
     * 主要是在服务通讯阶段顶一个安全监测，确认当前请求的协议是否合法。
     */
    private short magicNumber = MAGIC_NUMBER;

    /**
     * 协议传输核心数据的长度
     * 长度单独拿出来设置好处：
     * 当服务端的接收能力有限，可以对该字段进行赋值。当读取到的网络数据包中的contentLength字段已经超过逾期值的话，就不会去读取content字段。
     *
     */
    private int contentLength;

    /**
     * 核心的传输数据，这里核心的传输数据主要是请求的服务名称，请求服务的方法名称，请求参数内容。
     * 为了后期方便扩展这些核心的请求数据都统一封装到RpcInvocationl对象当中。
     * 
     * RpcInvocationl类中的字节数据
     *
     */
    private byte[] content;

    public RpcProtocol(byte[] content) {
        this.content = content;
        this.contentLength = content.length;
    }

    public short getMagicNumber() {
        return magicNumber;
    }

    public void setMagicNumber(short magicNumber) {
        this.magicNumber = magicNumber;
    }

    public int getContentLength() {
        return contentLength;
    }

    public void setContentLength(int contentLength) {
        this.contentLength = contentLength;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "RpcProtocol{" +
                "magicNumber=" + magicNumber +
                ", contentLength=" + contentLength +
                ", content=" + Arrays.toString(content) +
                '}';
    }
}

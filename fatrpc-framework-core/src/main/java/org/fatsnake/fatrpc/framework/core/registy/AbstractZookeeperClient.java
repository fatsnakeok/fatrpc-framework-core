package org.fatsnake.fatrpc.framework.core.registy;

import org.apache.zookeeper.Watcher;

import java.util.List;

/**
 * @Auther: fatsnake
 * @Description": 基于zookeeper的统一模板抽象类
 * @Date:2022/7/7 14:11
 * Copyright (c) 2022, zaodao All Rights Reserved.
 */
public abstract class AbstractZookeeperClient {

    private String zkAddress;
    /**
     * 基本睡眠时间
     */
    private int baseSleepTimes;
    /**
     * 最大重试次数
     */
    private int maxRetryTimes;


    public AbstractZookeeperClient(String zkAddress) {
        this.zkAddress = zkAddress;
        // 默认3000ms
        this.baseSleepTimes = 1000;
        this.maxRetryTimes = 3;
    }

    public AbstractZookeeperClient(String zkAddress, Integer baseSleepTimes, Integer maxRetryTimes) {
        this.zkAddress = zkAddress;
        if (baseSleepTimes == null) {
            this.baseSleepTimes = 1000;
        } else {
            this.baseSleepTimes = baseSleepTimes;
        }

        if (maxRetryTimes == null) {
            this.maxRetryTimes = 3;
        } else {
            this.maxRetryTimes = maxRetryTimes;
        }
    }

    public String getZkAddress() {
        return zkAddress;
    }

    public void setZkAddress(String zkAddress) {
        this.zkAddress = zkAddress;
    }

    public int getBaseSleepTimes() {
        return baseSleepTimes;
    }

    public void setBaseSleepTimes(int baseSleepTimes) {
        this.baseSleepTimes = baseSleepTimes;
    }

    public int getMaxRetryTimes() {
        return maxRetryTimes;
    }

    public void setMaxRetryTimes(int maxRetryTimes) {
        this.maxRetryTimes = maxRetryTimes;
    }

    ////////////////////留个子类实例化的方法//////////////////////

    public abstract void updateNodeData(String address, String data);

    public abstract Object getClient();

    /**
     * 拉取节点数据
     *
     * @param path
     * @return
     */
    public abstract String getNodeData(String path);


    /**
     * 获取指定目录下的子节点数据
     *
     * @param path
     * @return
     */
    public abstract List<String> getChildrenData(String path);


    /**
     * 创建持久化类型节点数据类型信息
     *
     * @param address
     * @param data
     */
    public abstract void createPersistentData(String address, String data);


    /**
     * 创建有序持久化类型节点数据类型信息
     *
     * @param address
     * @param data
     */
    public abstract void createPersistentWithSeqData(String address, String data);


    /**
     * 创建临时节点数据类型信息
     *
     * @param address
     * @param data
     */
    public abstract void createTemporaryData(String address, String data);


    /**
     * 设置某个节点的数值
     *
     * @param address
     * @param data
     */
    public abstract void setTemporaryData(String address, String data);


    /**
     * 断开zk的客户端链接
     */
    public abstract void destory();

    /**
     * 展示节点下的数据
     */
    public abstract List<String> listNode(String address);

    /**
     * 删除节点下边的数据
     *
     * @param address
     * @return
     */
    public abstract boolean deleteNode(String address);


    /**
     * @param address
     * @return
     */
    public abstract boolean existNode(String address);

    /**
     * 监听path路径下某个节点的数据变化
     *
     * @param path
     * @param watcher
     */
    public abstract void watchNodeData(String path, Watcher watcher);


    /**
     * 监听子节点下的数据变化
     *
     * @param path
     * @param watcher
     */
    public abstract void watchChildNodeData(String path, Watcher watcher);
}

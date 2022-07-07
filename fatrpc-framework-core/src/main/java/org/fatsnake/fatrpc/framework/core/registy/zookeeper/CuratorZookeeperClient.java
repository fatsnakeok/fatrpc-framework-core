package org.fatsnake.fatrpc.framework.core.registy.zookeeper;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.data.Stat;
import org.fatsnake.fatrpc.framework.core.registy.AbstractZookeeperClient;
import org.fatsnake.fatrpc.framework.core.registy.URL;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;

import static org.apache.zookeeper.Watcher.Event.EventType.NodeDeleted;

/**
 * @Auther: fatsnake
 * @Description":  针对CuratorFramework的具体实现
 * https://curator.apache.org/getting-started.html
 * @Date:2022/7/7 16:25
 * Copyright (c) 2022, zaodao All Rights Reserved.
 */
public class CuratorZookeeperClient extends AbstractZookeeperClient {


    private CuratorFramework client;

    public CuratorZookeeperClient(String  zkAddress) {
        super(zkAddress, null, null);
    }

    public CuratorZookeeperClient(String zkAddress, Integer baseSleepTimes, Integer maxRetryTimes) {
        super(zkAddress, baseSleepTimes, maxRetryTimes);
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(super.getBaseSleepTimes(), super.getMaxRetryTimes());
        if (client == null) {
            client = CuratorFrameworkFactory.newClient(zkAddress, retryPolicy);
            client.start();
        }
    }

    @Override
    public Object getClient() {
        return client;
    }

    @Override
    public void updateNodeData(String address, String data) {
        try {
            client.setData().forPath(address, data.getBytes(StandardCharsets.UTF_8));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    @Override
    public String getNodeData(String address) {
        try {
            byte[] result = client.getData().forPath(address);
            if (result != null) {
                return new String(result);
            }
        } catch (KeeperException.NoNodeException e) {
            return null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<String> getChildrenData(String path) {

        try {
            List<String> childrenData = client.getChildren().forPath(path);
            return childrenData;
        } catch (KeeperException.NoNodeException e) {
            return null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void createPersistentData(String address, String data) {

        try {
            client.create().creatingParentContainersIfNeeded().withMode(CreateMode.PERSISTENT)
                    .forPath(address, data.getBytes(StandardCharsets.UTF_8));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void createPersistentWithSeqData(String address, String data) {
        try {
            client.create().creatingParentContainersIfNeeded().withMode(CreateMode.PERSISTENT_SEQUENTIAL)
                    .forPath(address, data.getBytes(StandardCharsets.UTF_8));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void createTemporaryData(String address, String data) {
        try {
            client.create().creatingParentContainersIfNeeded().withMode(CreateMode.EPHEMERAL_SEQUENTIAL)
                    .forPath(address, data.getBytes(StandardCharsets.UTF_8));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setTemporaryData(String address, String data) {
        try {
            client.setData().forPath(address, data.getBytes(StandardCharsets.UTF_8));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void destory() {
        client.close();
    }

    @Override
    public List<String> listNode(String address) {
        try {
            return client.getChildren().forPath(address);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }

    @Override
    public boolean deleteNode(String address) {
        try {
            client.delete().forPath(address);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean existNode(String address) {
        try {
            Stat stat = client.checkExists().forPath(address);
            return stat != null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public void watchNodeData(String path, Watcher watcher) {
        try {
            client.getData().usingWatcher(watcher).forPath(path);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void watchChildNodeData(String path, Watcher watcher) {
        try {
            client.getChildren().usingWatcher(watcher).forPath(path);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        AbstractZookeeperClient abstractZookeeperClient = new CuratorZookeeperClient("localhost:2181");
        abstractZookeeperClient.watchNodeData("/fatrpc/org.fatsnake.fatrpc.framework.core.server.DataService/provider/10.1.21.11:9092",
                new Watcher() {
                    @Override
                    public void process(WatchedEvent watchedEvent) {
                        System.out.println(watchedEvent.getType());
                        if(NodeDeleted.equals(watchedEvent.getType())){
                            ProviderNodeInfo providerNodeInfo = URL.buildURLFromUrlStr(watchedEvent.getPath());
                            System.out.println(providerNodeInfo);
                        }
                    }
                });
        while (true){

        }
    }
}

package org.fatsnake.fatrpc.framework.core.registy.zookeeper;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.fatsnake.fatrpc.framework.core.common.event.IRpcEvent;
import org.fatsnake.fatrpc.framework.core.common.event.IRpcListenerLoader;
import org.fatsnake.fatrpc.framework.core.common.event.IRpcUpdateEvent;
import org.fatsnake.fatrpc.framework.core.common.event.data.URLChangeWrapper;
import org.fatsnake.fatrpc.framework.core.registy.RegistryService;
import org.fatsnake.fatrpc.framework.core.registy.URL;
import org.fatsnake.fatrpc.framework.core.server.DataService;

import java.util.List;

/**
 * @Auther: fatsnake
 * @Description": 对Zookeeper完成服务注册，服务订阅，服务下线等相关实际操作
 * @Date:2022/7/8 10:44
 * Copyright (c) 2022, zaodao All Rights Reserved.
 */
public class ZookeeperRegister extends AbstractRegister implements RegistryService {


    private AbstractZookeeperClient zkClient;

    /**
     * 根节点
     */
    private String ROOT = "/fatrpc";


    private String getProvicerPath(URL url) {
        return ROOT + "/" + url.getServiceName() + "/provider/" + url.getParameters().get("host") + ":" + url.getParameters().get("port");
    }

    private String getConsumerPath(URL url) {
        return ROOT + "/" + url.getServiceName() + "/consumer/" + url.getApplicationName() + ":" + url.getParameters().get("host") + ":";
    }

    @Override
    public List<String> getProviderIps(String serviceName) {
        List<String> nodeDataList = this.zkClient.getChildrenData(ROOT + "/" + serviceName + "/provider/");
        return nodeDataList;
    }

    public ZookeeperRegister(String address) {
        this.zkClient = new CuratorZookeeperClient(address);
    }

    @Override
    public void register(URL url) {
        // 初始化根节点
        if (!this.zkClient.existNode(ROOT)) {
            zkClient.createPersistentData(ROOT, "");
        }
        String uslStr = URL.buildProviderUrlStr(url);
        if (!zkClient.existNode(getProvicerPath(url))) {
            zkClient.createTemporaryData(getProvicerPath(url), uslStr);
        } else {
            zkClient.deleteNode(getProvicerPath(url));
            zkClient.createTemporaryData(getProvicerPath(url), uslStr);
        }
        // 更新本地缓存
        super.register(url);
    }

    @Override
    public void unRegister(URL url) {
        zkClient.deleteNode(getProvicerPath(url));
        // 更新本地缓存
        super.unRegister(url);
    }


    @Override
    public void subscribe(URL url) {
        if (!this.zkClient.existNode(ROOT)) {
            zkClient.createPersistentData(ROOT, "");
        }

        String urlStr = URL.buildConsumerUrlStr(url);
        if (!zkClient.existNode(getConsumerPath(url))) {
            zkClient.createTemporarySeqData(getConsumerPath(url), urlStr);
        } else {
            zkClient.deleteNode(getConsumerPath(url));
            zkClient.createTemporarySeqData(getConsumerPath(url), urlStr);
        }
        // 更新缓存
        super.subscribe(url);
    }

    @Override
    public void doAfterSubscribe(URL url) {
        // 监听是否又新的服务注册
        String newServerNodePath = ROOT + "/" + url.getServiceName() + "/provider";
        watchChildNodeData(newServerNodePath);
    }

    private void watchChildNodeData(String newServerNodePath) {
        zkClient.watchChildNodeData(newServerNodePath, new Watcher() {
            @Override
            public void process(WatchedEvent watchedEvent) {
                System.out.println(watchedEvent);
                String path = watchedEvent.getPath();
                List<String> childrenDataList = zkClient.getChildrenData(path);
                URLChangeWrapper urlChangeWrapper = new URLChangeWrapper();
                urlChangeWrapper.setProviderUrl(childrenDataList);
                urlChangeWrapper.setServiceName(path.split("/")[2]);
                // 当监听到某个节点的数据发生更新之后，会发送一个节点更新的事件，然后在事件的监听端对不同的行为做出不同的事件处理操作。
                IRpcEvent iRpcEvent = new IRpcUpdateEvent(urlChangeWrapper);
                //自定义的一套事件监听组件
                IRpcListenerLoader.sendEvent(iRpcEvent);
                //收到回调之后在注册一次监听，这样能保证一直都收到消息
                // 完成本次监听同事，注册下一次监听事件保证，事件总是有效

                // 此处zk的坑，因为zk节点的消息通知其实是只具有一次性的功效，所以可能会出现第一次修改节点之后发送一次通知，
                // 之后再次修改节点不再会发送节点变更通知操作。
                watchChildNodeData(path);
            }
        });

    }

    @Override
    public void doBeforeSubscribe(URL url) {

    }

    public void doUnSubscribe(URL url) {
        this.zkClient.deleteNode(getConsumerPath(url));
        // 更新缓存
        super.doUnSubscribe(url);
    }

    public static void main(String[] args) throws InterruptedException {
        ZookeeperRegister zookeeperRegister = new ZookeeperRegister("localhost:2181");
        List<String> urls = zookeeperRegister.getProviderIps(DataService.class.getName());
        System.out.println(urls);
        Thread.sleep(2000000);
    }


}

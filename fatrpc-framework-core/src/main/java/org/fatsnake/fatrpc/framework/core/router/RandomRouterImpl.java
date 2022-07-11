package org.fatsnake.fatrpc.framework.core.router;

import org.fatsnake.fatrpc.framework.core.common.ChannelFutureWrapper;
import org.fatsnake.fatrpc.framework.core.registy.URL;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.fatsnake.fatrpc.framework.core.common.cache.CommonClientCache.CHANNEL_FUTURE_POLLING_REF;
import static org.fatsnake.fatrpc.framework.core.common.cache.CommonClientCache.CONNECT_MAP;
import static org.fatsnake.fatrpc.framework.core.common.cache.CommonClientCache.SERVICE_ROUTER_MAP;

/**
 * @Auther: fatsnake
 * @Description": 随机调用实现
 * @Date:2022/7/11 11:26
 * Copyright (c) 2022, zaodao All Rights Reserved.
 */
public class RandomRouterImpl implements IRouter {
    @Override
    public void refreshRouterArr(Selector selector) {
        // 获取服务提供者的数目
        List<ChannelFutureWrapper> channelFutureWrapperList = CONNECT_MAP.get(selector.getProviderServiceName());
        ChannelFutureWrapper[] arr = new ChannelFutureWrapper[channelFutureWrapperList.size()];
        // 提前生成调用先后顺序的随机数组
        int[] result = createRandomIndex(arr.length);
        // 生成对应服务集群的每台机器的调用顺序
        for (int i = 0; i < result.length; i++) {
            arr[i] = channelFutureWrapperList.get(result[i]);
        }
        SERVICE_ROUTER_MAP.put(selector.getProviderServiceName(), arr);
    }


    @Override
    public ChannelFutureWrapper select(Selector selector) {
        return CHANNEL_FUTURE_POLLING_REF.getChannelFutureWrapper(selector.getProviderServiceName());
    }

    @Override
    public void updateWeight(URL url) {
        // 服务节点的权重
        List<ChannelFutureWrapper> channelFutureWrapperList = CONNECT_MAP.get(url.getServiceName());
        Integer[] weightArr = createWeightArr(channelFutureWrapperList);
        Integer[] finalArr = createRandomArr(weightArr);
        ChannelFutureWrapper[] finalChannelFutureWrapperList = new ChannelFutureWrapper[finalArr.length];
        for (int j = 0; j < finalChannelFutureWrapperList.length; j++) {
            finalChannelFutureWrapperList[j] = channelFutureWrapperList.get(finalArr[j]);
        }
        SERVICE_ROUTER_MAP.put(url.getServiceName(), finalChannelFutureWrapperList);

    }

    public static void main(String[] args) {
        List<ChannelFutureWrapper> channelFutureWrappers = new ArrayList<>();
        channelFutureWrappers.add(new ChannelFutureWrapper(null, null, 100));
        channelFutureWrappers.add(new ChannelFutureWrapper(null, null, 200));
        channelFutureWrappers.add(new ChannelFutureWrapper(null, null, 9300));
        channelFutureWrappers.add(new ChannelFutureWrapper(null, null, 400));
        Integer[] r = createWeightArr(channelFutureWrappers);
        System.out.println(r);
    }

    /**
     * 创建随机乱序数组：对传入数据，进行乱序排序
     *
     * @param arr
     * @return Integer
     */
    private static Integer[] createRandomArr(Integer[] arr) {
        int total = arr.length;
        Random ra = new Random();
        for (int i = 0; i < total; i++) {
            int j = ra.nextInt(total);
            if (i == j) {
                continue;
            }
            int temp = arr[i];
            arr[i] = arr[j];
            arr[j] = temp;
        }
        return arr;
    }

    /**
     * 创建Weight/100个数组
     *
     * @param channelFutureWrapperList
     * @return
     */
    private static Integer[] createWeightArr(List<ChannelFutureWrapper> channelFutureWrapperList) {
        List<Integer> weightArr = new ArrayList<>();
        for (int k = 0; k < channelFutureWrapperList.size(); k++) {
            Integer weight = channelFutureWrapperList.get(k).getWeight();
            int c = weight / 100;
            for (int i = 0; i < c; i++) {
                weightArr.add(k);
            }
        }
        Integer[] arr = new Integer[weightArr.size()];
        // 转换出指定泛型的 数组
        return weightArr.toArray(arr);
    }

    private int[] createRandomIndex(int len) {
        int[] arrInt = new int[len];
        Random ra = new Random();
        // 放入初始值，用于contains方法判断
        for(int i=0;i < arrInt.length; i++) {
            arrInt[i] = -1;
        }
        int index = 0;
        while (index < arrInt.length) {
            int num = ra.nextInt(len);
            // 如果数组中不包含这个元素则赋值给数组
            // 如果当前下标的值存在，下标将不会递增，继续循环下去，直到出现数组中不存在的值
            if(!contains(arrInt, num)) {
                // index++,先复制后递增
                arrInt[index++] = num;
            }
        }
        return arrInt;
    }

    public boolean contains(int[] arr, int key) {
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] == key) {
                return true;
            }
        }
        return false;
    }
}

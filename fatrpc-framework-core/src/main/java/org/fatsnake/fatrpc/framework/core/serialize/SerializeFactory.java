package org.fatsnake.fatrpc.framework.core.serialize;

/**
 * @Auther: fatsnake
 * @Description": 兼容不同序列化计划，此处抽象出一层
 * @Date:2022/7/13 9:55 下午
 * Copyright (c) 2022, zaodao All Rights Reserved.
 */
public interface SerializeFactory {

    /**
     * 序列化
     *
     * @param t
     * @param <T>
     * @return
     */
    <T> byte[] serialize(T t);


    /**
     * 反序列化
     *
     * @param data
     * @param clazz
     * @param <T>
     * @return
     */
    <T> T deserialize(byte[] data, Class<T> clazz);

}

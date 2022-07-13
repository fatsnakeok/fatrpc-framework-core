package org.fatsnake.fatrpc.framework.core.serialize.fastjson;

import com.alibaba.fastjson.JSON;
import org.fatsnake.fatrpc.framework.core.serialize.SerializeFactory;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;

/**
 * @Auther: fatsnake
 * @Description":
 * @Date:2022/7/13 10:12 下午
 * Copyright (c) 2022, zaodao All Rights Reserved.
 */
public class FastJsonSerializeFactory implements SerializeFactory {
    @Override
    public <T> byte[] serialize(T t) {
        String jsonStr = JSON.toJSONString(t);
        return jsonStr.getBytes(StandardCharsets.UTF_8);
    }

    @Override
    public <T> T deserialize(byte[] data, Class<T> clazz) {
        return JSON.parseObject(new String(data), clazz);
    }
}

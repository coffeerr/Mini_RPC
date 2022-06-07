package com.coffeerr.serialize.impl;

import com.alibaba.fastjson.JSON;
import com.coffeerr.constants.SerializeCode;
import com.coffeerr.serialize.CommonSerializer;

/**
 * @description:
 * @author: Desmond
 * @time: 2022/4/7 8:31 下午
 */

public class JsonSerializer implements CommonSerializer {
    public byte[] serialize(Object object) {
        return JSON.toJSONBytes(object);
    }

    public Object deserialize(byte[] bytes, Class<?> clazz) {
        return JSON.parseObject(bytes, clazz);
    }

    @Override
    public int getCode() {
        return SerializeCode.JSON.getCode();
    }
}

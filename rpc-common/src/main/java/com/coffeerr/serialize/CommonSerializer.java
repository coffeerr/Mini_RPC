package com.coffeerr.serialize;

import com.coffeerr.serialize.impl.JsonSerializer;

public interface CommonSerializer {
    // 默认

    static CommonSerializer getSerializeByCode(int code) {
        switch (code) {
            case 1:
                return new JsonSerializer();
            default:
                return new JsonSerializer();
        }
    }


    byte[] serialize(Object object);

    Object deserialize(byte[] bytes, Class<?> clazz);
}

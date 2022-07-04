package com.coffeerr.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @description:
 * @author: Desmond
 * @time: 2022/4/7 6:04 下午
 */

public enum SerializeCode {
    DEFAULT(0),
    JSON(1),
    KRYO(2),

    HESSIAN(3),

    PROTOSTUFF(4);
    private int code;

    private SerializeCode(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}

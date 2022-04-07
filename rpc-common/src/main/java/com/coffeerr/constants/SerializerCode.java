package com.coffeerr.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @description:
 * @author: Desmond
 * @time: 2022/4/7 6:04 下午
 */

@AllArgsConstructor
@Getter
public enum SerializerCode {
    DEFAULT(0),
    JSON(1);
    private int code;
}

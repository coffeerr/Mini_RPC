package com.coffeerr.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum RpcResponseCode {
    SUCCESS(200, "请求成功"),
    FAIL(201, "请求失败");

    private final int code;
    private final String message;
}

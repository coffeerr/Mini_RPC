package com.coffeerr.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @description:
 * @author: Desmond
 * @time: 2022/4/7 8:21 下午
 */
@AllArgsConstructor
@Getter
public enum RpcError {
    MAGIC_NUMBER_ERROR("魔数错误"),
    MESSAGE_ID_ERROR("消息ID错误"),
    PROTOCOL_VERSION_ERROR("协议版本错误"),
    PACKAGE_TYPE_ERROR("消息类型错误");

    private String message;
}

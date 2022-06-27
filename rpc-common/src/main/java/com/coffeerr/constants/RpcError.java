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
    SERVICE_NOT_IMPLEMENT_ANY_INTERFACE("服务未实现接口"),
    SERVICE_NOT_REGISTERED("服务未注册"),
    SERIALIZER_NOT_FOUND("序列化器未找到"),
    SERIALIZE_ERROR("序列化时有错误发生"),
    DESERIALIZE_ERROR("反序列化时有错误发生"),
    PACKAGE_TYPE_ERROR("消息类型错误"),

    FAILED_TO_CONNECT_TO_SERVICE_REGISTRY("连接到注册中心时发生错误");

    private String message;
}

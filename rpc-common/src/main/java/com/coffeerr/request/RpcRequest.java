package com.coffeerr.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @description:
 * @author: Desmand
 * @time: 2022/4/7 11:16 上午
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RpcRequest implements Serializable {
    // 调用的接口名，方法名，参数，参数类型
    private String interfaceName;

    private String methodName;

    private Object[] parameters;

    private Class<?>[] parameterType;
}

package com.coffeerr.client;

import com.coffeerr.client.impl.RpcClient;
import com.coffeerr.client.impl.RpcClientProtocolHandler;
import com.coffeerr.request.RpcRequest;
import com.coffeerr.respose.RpcResponse;
import com.coffeerr.serialize.CommonSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @description:
 * @author: Desmand
 * @time: 2022/4/7 2:04 下午
 */
@Data
@Getter
@AllArgsConstructor
public class RpcClientProxy implements InvocationHandler {
    private int port;
    private String host;
    private CommonSerializer serializer;


    public <T> T getProxy(Class<T> clazz) {
        return (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class<?>[]{clazz}, this);
    }


    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        RpcRequest rpcRequest = RpcRequest.builder()
                .interfaceName(method.getDeclaringClass().getName())
                .methodName(method.getName())
                .parameters(args)
                .parameterType(method.getParameterTypes())
                .build();
        RpcClientService rpcClient = new RpcClientProtocolHandler();
        return rpcClient.sendRequest(rpcRequest, host, port, serializer);
    }


}

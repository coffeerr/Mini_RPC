# Mini_RPC
>  mini版本的RPC框架



### V1.0

首先，我们完成一个可以实现利用Socket实现简单通信的RPC，客户端调用`HelloService`方法进行验证。

为了实现通信，我们需要实现：

①、api的编写，不需要很复杂，只要可以验证即可

②、传输对象：`RPCRequest`、`RPCResponse`

③、通信框架：socket实现

④、客户端实现：动态代理

⑤、服务端实现：线程池处理多次请求

#### 测试结果

服务端：
![server](https://markdown-image-1257239969.cos.ap-nanjing.myqcloud.com/2022/04/07/jie-ping20220407-xia-wu45952.png)

客户端：
![client](https://markdown-image-1257239969.cos.ap-nanjing.myqcloud.com/2022/04/07/jie-ping20220407-xia-wu50114.png)

#### 难点：

1、客户端，如何通过动态代理实现方法调用？

2、服务端，如何根据传过来的RpcRequest调用对应的方法？

```java
Method method = service.getClass().getMethod(rpcRequest.getMethodName(), rpcRequest.getParameterType());
Object res = method.invoke(service, rpcRequest.getParameters());
```

### V1.1
①、优化服务端实现方式，将线程抽离

②、引入协议，通过协议进行消息通信

关于协议的设定，参考了[《RPC实战与核心原理》](https://time.geekbang.org/column/article/199651)中的协议设定
即 **｜魔术位 ｜ 长度 ｜ 消息ID ｜ 协议版本 ｜ 消息类型 ｜ 序列化方式｜数据｜ **的表示方式。 

### V1.2
①、服务端：利用容器存储服务，可满足同时注册多个服务

### V1.3
①、Netty：引入Netty，利用Netty替代Socket实现网络传输。
②、扩充序列化协议：Kryo

##GTD
### V1.4
①、使用Nacos作为注册中心
②、支持更多的序列化协议


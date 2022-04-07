# Mini_RPC
mini版本的RPC框架



### V1.0

首先，我们完成一个可以实现利用Socket实现简单通信的RPC，客户端调用`HelloService`方法进行验证。

为了实现通信，我们需要实现：

①、api的编写，不需要很复杂，只要可以验证即可

②、传输对象：`RPCRequest`、`RPCResponse`

③、协议配置

④、通信框架：socket实现 + Netty实现

⑤、客户端实现：动态代理

### V2.0




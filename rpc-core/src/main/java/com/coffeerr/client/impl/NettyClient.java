package com.coffeerr.client.impl;

import com.coffeerr.client.CommonClient;
import com.coffeerr.client.NettyClientHandler;
import com.coffeerr.codec.CommonDecoder;
import com.coffeerr.codec.CommonEncoder;
import com.coffeerr.registry.ServiceDiscovery;
import com.coffeerr.registry.impl.NacosServiceDiscoveryImpl;
import com.coffeerr.request.RpcRequest;
import com.coffeerr.respose.RpcResponse;
import com.coffeerr.serialize.CommonSerializer;
import com.coffeerr.serialize.impl.KryoSerializer;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.AttributeKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @description:
 * @author: Desmond
 * @time: 2022/6/28 10:54 AM
 */

public class NettyClient implements CommonClient {

    private static final Logger logger = LoggerFactory.getLogger(com.coffeerr.client.NettyClient.class);

    private String host;
    private int port;
    private static final Bootstrap bootstrap;

    private CommonSerializer commonSerializer;

    private ServiceDiscovery serviceDiscovery;

    public NettyClient(String host, int port) {
        this.host = host;
        this.port = port;
        this.serviceDiscovery = new NacosServiceDiscoveryImpl();
    }

    static {
        EventLoopGroup group = new NioEventLoopGroup();
        bootstrap = new Bootstrap();
        bootstrap.group(group)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ChannelPipeline pipeline = ch.pipeline();
                        pipeline.addLast(new CommonDecoder())
                                .addLast(new CommonEncoder(new KryoSerializer()))
                                .addLast(new NettyClientHandler());
                    }
                });
    }

    @Override
    public Object sendRequest(RpcRequest rpcRequest) {
        try {
            ChannelFuture future = bootstrap.connect(host, port).sync();
            logger.info("客户端连接到服务端{}：{}", host, port);
            Channel channel = future.channel();
            if (channel != null) {
                //向服务端发请求，并设置监听，关于writeAndFlush()的具体实现可以参考：https://blog.csdn.net/qq_34436819/article/details/103937188
                channel.writeAndFlush(rpcRequest).addListener(future1 -> {
                    if (future1.isSuccess()) {
                        logger.info(String.format("客户端发送消息：%s", rpcRequest.toString()));
                    } else {
                        logger.error("发送消息时有错误发生:", future1.cause());
                    }
                });
                channel.closeFuture().sync();
                //AttributeMap<AttributeKey, AttributeValue>是绑定在Channel上的，可以设置用来获取通道对象
                AttributeKey<RpcResponse> key = AttributeKey.valueOf("rpcResponse");
                //get()阻塞获取value
                RpcResponse rpcResponse = channel.attr(key).get();
                return rpcResponse.getData();
            }
        } catch (InterruptedException e) {
            logger.error("发送消息时有错误发生:", e);
        }
        return null;
    }

    @Override
    public void setSerializer(CommonSerializer commonSerializer) {
        this.commonSerializer = commonSerializer;
    }
}

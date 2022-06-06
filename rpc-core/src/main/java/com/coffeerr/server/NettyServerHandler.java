package com.coffeerr.server;

import com.coffeerr.registry.ServiceRegistry;
import com.coffeerr.registry.impl.ServiceRegistryImpl;
import com.coffeerr.request.RpcRequest;
import com.coffeerr.respose.RpcResponse;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.ReferenceCountUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

/**
 * @description:
 * @author: Desmond
 * @time: 2022/6/6 2:32 下午
 */

public class NettyServerHandler extends SimpleChannelInboundHandler<RpcRequest> {

    private static final Logger logger = LoggerFactory.getLogger(NettyServerHandler.class);
    private static ServiceRegistry serviceRegistry;

    static {
        serviceRegistry = new ServiceRegistryImpl();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcRequest rpcRequest) throws Exception {
        try {
            logger.info("服务端接收到请求：{}", rpcRequest);
            String interfaceName = rpcRequest.getInterfaceName();
            Object service = serviceRegistry.getService(interfaceName);

            Method method = service.getClass().getMethod(rpcRequest.getMethodName(), rpcRequest.getParameterType());
            Object result = method.invoke(service, rpcRequest.getParameters());
            ChannelFuture future = ctx.writeAndFlush(RpcResponse.success(result));
            //添加一个监听器到channelfuture来检测是否所有的数据包都发出，然后关闭通道
            future.addListener(ChannelFutureListener.CLOSE);
        } finally {
            ReferenceCountUtil.release(rpcRequest);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.error("处理过程调用时有错误发生：");
        cause.printStackTrace();
        ctx.close();
    }
}

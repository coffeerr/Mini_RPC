package com.coffeerr.server.impl;

import com.coffeerr.HelloObject;
import com.coffeerr.HelloService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @description:
 * @author: Desmand
 * @time: 2022/4/7 2:56 下午
 */

public class HelloServiceImpl implements HelloService {
    public static final Logger logger = LoggerFactory.getLogger(HelloServiceImpl.class);

    public String hello(HelloObject helloObject) {
        logger.info("接收到：{}", helloObject.getMessage());
        return "来自服务端的招手";
    }
}

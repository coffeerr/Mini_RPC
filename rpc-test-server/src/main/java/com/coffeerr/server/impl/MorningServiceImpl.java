package com.coffeerr.server.impl;

import com.coffeerr.HelloObject;
import com.coffeerr.MorningService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @description:
 * @author: Desmond
 * @time: 2022/6/6 11:00 上午
 */

public class MorningServiceImpl implements MorningService {
    public static final Logger logger = LoggerFactory.getLogger(MorningServiceImpl.class);

    public String hello(HelloObject helloObject) {
        logger.info("MorningServiceImpl");
        logger.info("接收到：{}", helloObject.getMessage());
        return "来自服务端的招手";
    }
}

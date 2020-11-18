package com.github.johnsonmoon.test.obj;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Create by xuyh at 2020/2/19 15:18.
 */
public class TestFieldObject {
    private static Logger logger = LoggerFactory.getLogger(TestFieldObject.class);

    private AtomicLong count = new AtomicLong(0);

    public String test() {
        String msg = String.format("TestFieldObject test: %s", count.getAndIncrement());
        logger.info(msg);
        return msg;
    }

    public String test2(String abc, String def) {
        logger.info("TestFieldObject test2: {}, params: {} {}", count.getAndIncrement(), abc, def);
        return "TestFieldObject test2";
    }
}

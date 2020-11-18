package com.github.johnsonmoon.test.obj;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Create by xuyh at 2020/2/14 10:57.
 */
public class TestObject {
    private static Logger logger = LoggerFactory.getLogger(TestObject.class);

    public void test1() {
        logger.info("Simple test method without parameters, without return value.");
    }

    public void test2(String abc, String def) {
        logger.info("Simple test method with parameters, without return value.");
    }

    public String test3(String abc, String def, String ghi) {
        logger.info("Simple test method with parameters, with return value.");
        return "Hello!";
    }

    public String test4(String abc) {
        logger.info("Simple test method with parameters, with return value, with throwable.");
        throw new RuntimeException("test exception message: abc");
    }
}

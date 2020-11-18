package com.github.johnsonmoon.test.obj;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Create by xuyh at 2020/2/14 15:52.
 */
public class TestObjectRet {
    private static Logger logger = LoggerFactory.getLogger(TestObjectRet.class);

    public String test(String abc) {
        String abcS = "Message: " + abc;
        logger.info("Simple test method with parameters, without return value.");
        return abcS;
    }
}

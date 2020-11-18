package com.github.johnsonmoon.test.obj;

import com.github.johnsonmoon.test.annotation.TestAnnotation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Create by xuyh at 2020/2/14 18:48.
 */
@TestAnnotation(name = "TestObjectCopy")
public class TestObjectCopy {
    private static Logger logger = LoggerFactory.getLogger(TestObjectCopy.class);

    @TestAnnotation(name = "TestObjectCopy", desc = "method task")
    public void task(@TestAnnotation(name = "name") String name,
                     @TestAnnotation(name = "count") Integer count) throws Exception {
        logger.info("TestObjectCopy: do task {}", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
    }
}

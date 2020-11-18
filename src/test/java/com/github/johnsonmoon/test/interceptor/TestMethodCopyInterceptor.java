package com.github.johnsonmoon.test.interceptor;

import com.github.johnsonmoon.common.interceptor.Interceptor;
import com.github.johnsonmoon.common.interceptor.MethodCopy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Create by xuyh at 2020/2/14 20:04.
 */
@MethodCopy(targetClass = "com.github.johnsonmoon.test.obj.TestObjectCopy", targetMethod = "task")
public class TestMethodCopyInterceptor implements Interceptor {
    private static Logger logger = LoggerFactory.getLogger(TestMethodCopyInterceptor.class);

    @Override
    public void before(Object[] params) {
        logger.info("TestMethodCopyInterceptor before. param {}", params);
    }

    @Override
    public void after(Object[] params, Object retValue) {
        logger.info("TestMethodCopyInterceptor after. param {}, retValue {}", params, retValue);
    }

    @Override
    public void afterThrow(Object[] params, Throwable e) {
        logger.info("TestMethodCopyInterceptor after throw. param {}, throwable {}", params, e);
    }
}

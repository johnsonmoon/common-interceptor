package com.github.johnsonmoon.test.interceptor;

import com.github.johnsonmoon.common.interceptor.Intercept;
import com.github.johnsonmoon.common.interceptor.Interceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Create by xuyh at 2020/2/14 10:57.
 */
@Intercept(targetClass = "com.github.johnsonmoon.test.obj.TestObject", targetMethod = "test4")
public class Test4Interceptor implements Interceptor {
    private static Logger logger = LoggerFactory.getLogger(Test4Interceptor.class);

    @Override
    public void before(Object[] params) {
        logger.info("Before, interceptor {}, params {}", 4, params);
    }

    @Override
    public void after(Object[] params, Object retValue) {
        logger.info("After, interceptor {}, params {}, retValue {}", 4, params, retValue);
    }

    @Override
    public void afterThrow(Object[] params, Throwable e) {
        logger.info("After throw, interceptor {}, params {}, throwable {}, throwable message: {}", 4, params, e, e.getMessage());
    }
}

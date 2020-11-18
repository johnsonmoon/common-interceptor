package com.github.johnsonmoon.test.interceptor;

import com.github.johnsonmoon.common.interceptor.Intercept;
import com.github.johnsonmoon.common.interceptor.Interceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Create by xuyh at 2020/2/17 19:41.
 */
@Intercept(
        targetClass = "com.github.johnsonmoon.test.obj.TestObjectConstructor",
        isConstructor = true,
        constructorParameterTypes = {
                "java.util.concurrent.atomic.AtomicLong"
        }
)
public class ConstructorInterceptor1 implements Interceptor {
    private static Logger logger = LoggerFactory.getLogger(ConstructorInterceptor1.class);

    @Override
    public void before(Object[] params) {
        logger.info("ConstructorInterceptor1: before, param: {}", params);
    }

    @Override
    public void after(Object[] params, Object retValue) {
        logger.info("ConstructorInterceptor1: after, param: {}, retValue: {}", params, retValue);
    }

    @Override
    public void afterThrow(Object[] params, Throwable e) {
        logger.info("ConstructorInterceptor1: after, param: {}, throwable: {}", params, e);
    }
}

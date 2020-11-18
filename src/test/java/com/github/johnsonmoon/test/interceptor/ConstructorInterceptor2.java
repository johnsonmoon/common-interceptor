package com.github.johnsonmoon.test.interceptor;

import com.github.johnsonmoon.common.interceptor.Intercept;
import com.github.johnsonmoon.common.interceptor.ResultInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Create by xuyh at 2020/2/17 19:42.
 */
@Intercept(
        targetClass = "com.github.johnsonmoon.test.obj.TestObjectConstructor2",
        isConstructor = true,
        constructorParameterTypes = {
                "java.util.concurrent.atomic.AtomicLong",
                "java.lang.String"
        }
)
public class ConstructorInterceptor2 implements ResultInterceptor {
    private static Logger logger = LoggerFactory.getLogger(ConstructorInterceptor2.class);

    @Override
    public Object after(Object[] params, Object retValue) {
        logger.info("ConstructorInterceptor2: after, params: {}, retValue: {}", params, retValue);
        return retValue;
    }
}

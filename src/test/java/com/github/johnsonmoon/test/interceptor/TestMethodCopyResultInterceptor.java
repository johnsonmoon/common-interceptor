package com.github.johnsonmoon.test.interceptor;

import com.github.johnsonmoon.common.interceptor.MethodCopy;
import com.github.johnsonmoon.common.interceptor.ResultInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Create by xuyh at 2020/2/14 20:04.
 */
@MethodCopy(targetClass = "com.github.johnsonmoon.test.obj.TestObjectCopy", targetMethod = "task", newMethodName = "taskCopy2")
public class TestMethodCopyResultInterceptor implements ResultInterceptor {
    private static Logger logger = LoggerFactory.getLogger(TestMethodCopyResultInterceptor.class);

    @Override
    public Object after(Object[] params, Object retValue) {
        logger.info("TestMethodCopyResultInterceptor after. param {}, retValue {}", params, retValue);
        return retValue;
    }
}

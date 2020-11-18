package com.github.johnsonmoon.test.interceptor;

import com.github.johnsonmoon.common.interceptor.Intercept;
import com.github.johnsonmoon.common.interceptor.ResultInterceptor;

/**
 * Create by xuyh at 2020/2/14 15:54.
 */
@Intercept(targetClass = "com.github.johnsonmoon.test.obj.TestObjectRet", targetMethod = "test")
public class TestResultInterceptor implements ResultInterceptor {
    @Override
    public Object after(Object[] params, Object retValue) {
        String ret_value = "Append, " + retValue;
        return ret_value;
    }
}

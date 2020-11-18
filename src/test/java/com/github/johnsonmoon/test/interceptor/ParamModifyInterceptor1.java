package com.github.johnsonmoon.test.interceptor;

import com.github.johnsonmoon.common.interceptor.ParameterIntercept;
import com.github.johnsonmoon.common.interceptor.ParameterInterceptor;

/**
 * Create by xuyh at 2020/2/17 23:41.
 */
@ParameterIntercept(
        targetClass = "com.github.johnsonmoon.test.obj.TestParamModifyObject",
        targetMethod = "setAbc",
        replacementParameterIndex = 0,
        replacementParameterTypeName = "java.lang.String"
)
public class ParamModifyInterceptor1 implements ParameterInterceptor {
    @Override
    public Object before(Object[] params) {
        return "Hello";
    }
}

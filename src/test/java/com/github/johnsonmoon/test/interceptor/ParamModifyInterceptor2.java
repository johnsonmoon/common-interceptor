package com.github.johnsonmoon.test.interceptor;

import com.github.johnsonmoon.common.interceptor.ParameterIntercept;
import com.github.johnsonmoon.common.interceptor.ParameterInterceptor;

/**
 * Create by xuyh at 2020/2/17 23:43.
 */
@ParameterIntercept(
        targetClass = "com.github.johnsonmoon.test.obj.TestParamModifyObject",
        isConstructor = true,
        constructorParameterTypes = {
                "java.lang.String",
                "java.lang.String"
        },
        replacementParameterIndex = 1,
        replacementParameterTypeName = "java.lang.String"
)
public class ParamModifyInterceptor2 implements ParameterInterceptor {
    @Override
    public Object before(Object[] params) {
        return "Hello";
    }
}

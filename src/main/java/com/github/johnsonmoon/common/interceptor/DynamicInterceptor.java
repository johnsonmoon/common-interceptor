package com.github.johnsonmoon.common.interceptor;

/**
 * Create by xuyh at 2020/2/19 15:07.
 */
public interface DynamicInterceptor {
    Object[] before(String injectionPoint, Object[] params);

    Object after(String injectionPoint, Object[] params, Object retValue);
}

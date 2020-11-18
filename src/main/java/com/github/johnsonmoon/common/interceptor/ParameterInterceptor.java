package com.github.johnsonmoon.common.interceptor;

/**
 * Change parameter
 * <p>
 * Create by xuyh at 2020/2/17 22:43.
 */
public interface ParameterInterceptor {
    Object before(Object[] params);
}

package com.github.johnsonmoon.common.interceptor;

/**
 * Create by xuyh at 2020/2/9 17:20.
 */
public interface Interceptor {
    void before(Object[] params);

    void after(Object[] params, Object retValue);

    void afterThrow(Object[] params, Throwable e);
}

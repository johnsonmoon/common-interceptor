package com.github.johnsonmoon.common.interceptor;

/**
 * Create by xuyh at 2020/2/14 15:45.
 */
public interface ResultInterceptor {
    Object after(Object[] params, Object retValue);
}

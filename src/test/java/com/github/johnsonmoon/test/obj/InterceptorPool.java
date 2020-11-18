package com.github.johnsonmoon.test.obj;

import com.github.johnsonmoon.common.interceptor.ResultInterceptor;

import java.util.HashMap;
import java.util.Map;

/**
 * Create by xuyh at 2020/2/19 15:20.
 */
public class InterceptorPool {
    private static Map<String, ResultInterceptor> resultInterceptorMap = new HashMap<>();

    public static void putInterceptor(String id, ResultInterceptor resultInterceptor) {
        resultInterceptorMap.put(id, resultInterceptor);
    }

    public static ResultInterceptor getInterceptor(String id) {
        return resultInterceptorMap.get(id);
    }
}

package com.github.johnsonmoon.common.interceptor;

import java.util.HashMap;
import java.util.Map;

/**
 * Create by xuyh at 2020/2/19 15:38.
 */
public class DynamicInterceptorPool {
    private static Map<String, DynamicInterceptor> interceptorMap = new HashMap<>();

    public static DynamicInterceptor get(String id) {
        return interceptorMap.get(id);
    }

    public static void put(String id, DynamicInterceptor interceptor) {
        interceptorMap.put(id, interceptor);
    }
}

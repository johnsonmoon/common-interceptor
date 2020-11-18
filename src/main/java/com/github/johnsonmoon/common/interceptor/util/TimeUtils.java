package com.github.johnsonmoon.common.interceptor.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;


/**
 * Create by xuyh at 2020/2/13 11:11.
 */
public class TimeUtils {
    private static Logger logger = LoggerFactory.getLogger(TimeUtils.class);

    public static void sleep(long timemillis) {
        try {
            TimeUnit.MILLISECONDS.sleep(timemillis);
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
        }
    }
}

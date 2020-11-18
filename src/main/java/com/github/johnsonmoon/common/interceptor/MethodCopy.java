package com.github.johnsonmoon.common.interceptor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Create by xuyh at 2020/2/14 18:05.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface MethodCopy {
    String targetClass() default "";

    String targetMethod() default "";

    String newMethodName() default "";
}

package com.github.johnsonmoon.common.interceptor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Create by xuyh at 2020/2/12 21:21.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Intercept {
    String targetClass() default "";

    String targetMethod() default "";

    boolean isConstructor() default false;

    String[] constructorParameterTypes() default {};
}

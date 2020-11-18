package com.github.johnsonmoon.common.interceptor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Create by xuyh at 2020/2/17 23:06.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ParameterIntercept {
    /**
     * starts from 0
     */
    int replacementParameterIndex();

    /**
     * "java.lang.String"
     * "javax.sql.Connection"
     * "javax.sql.DataSource"
     * etc.
     */
    String replacementParameterTypeName();

    String targetClass() default "";

    String targetMethod() default "";

    boolean isConstructor() default false;

    String[] constructorParameterTypes() default {};
}

package com.github.johnsonmoon.test.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Create by xuyh at 2020/2/15 11:23.
 */
@Target({ElementType.TYPE,
        ElementType.ANNOTATION_TYPE,
        ElementType.CONSTRUCTOR,
        ElementType.FIELD,
        ElementType.METHOD,
        ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface TestAnnotation {
    String name();

    String desc() default "";
}

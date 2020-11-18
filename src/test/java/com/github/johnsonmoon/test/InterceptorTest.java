package com.github.johnsonmoon.test;

import com.github.johnsonmoon.common.interceptor.DynamicClassEnhanceInitializer;
import com.github.johnsonmoon.common.interceptor.DynamicInterceptor;
import com.github.johnsonmoon.common.interceptor.InterceptorInitializer;
import com.github.johnsonmoon.test.obj.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Create by xuyh at 2020/2/14 10:56.
 */
public class InterceptorTest {
    private static Logger logger = LoggerFactory.getLogger(InterceptorTest.class);

    public static void main(String[] args) {
        InterceptorInitializer.init("com.github.johnsonmoon.test.interceptor");
        try {
            new TestObject().test1();
            new TestObject().test2("abc", "def");
            System.out.println(new TestObject().test3("abc", "def", "ghi"));
            System.out.println(new TestObject().test4("abc"));
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
        }
        //----
        try {
            System.out.println(new TestObjectRet().test("abc"));
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
        }
        //----
        try {
            new TestObjectCopy().task("abc", 12);
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
        }
        //----
        try {
            System.out.println(new TestObjectConstructor(new AtomicLong(0)).getFlag());
            System.out.println(new TestObjectConstructor2(new AtomicLong(0), "test").getFlag());
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
        }
        //----
        try {
            TestParamModifyObject object = new TestParamModifyObject("abc", "def");
            object.setAbc("aabbcc");
            System.out.println(object.toString());
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
        }
        //----
        try {
            DynamicClassEnhanceInitializer.doEnhance(
                    "com.github.johnsonmoon.test.obj.TestFieldObject",
                    "test2",
                    new DynamicInterceptor() {
                        @Override
                        public Object[] before(String injectionPoint, Object[] params) {
                            logger.info("InjectionPoint: {}", injectionPoint);
                            logger.info("before!");
                            return new Object[]{"Hello!", "Hello2!"};
                        }

                        @Override
                        public Object after(String injectionPoint, Object[] params, Object retValue) {
                            logger.info("InjectionPoint: {}", injectionPoint);
                            logger.info("after!");
                            return retValue;
                        }
                    });
            logger.info(new TestFieldObject().test2("abc", "def"));
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
        }
    }
}

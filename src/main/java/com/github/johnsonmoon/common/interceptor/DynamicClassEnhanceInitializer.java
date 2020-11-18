package com.github.johnsonmoon.common.interceptor;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Create by xuyh at 2020/2/19 15:04.
 */
public class DynamicClassEnhanceInitializer {
    private static Logger logger = LoggerFactory.getLogger(DynamicClassEnhanceInitializer.class);

    public static boolean doEnhance(String targetClass, String targetMethod,
                                    DynamicInterceptor interceptor) {
        if (targetClass == null || targetClass.isEmpty()
                || targetMethod == null || targetMethod.isEmpty()
                || interceptor == null) {
            logger.warn("Param [targetClass] or [targetMethod] or [interceptor] must not be null.");
            return false;
        }
        String injectionPoint = targetClass + "#" + targetMethod;
        logger.info("DynamicClassEnhanceInitializer enhance class {} ...", targetClass);
        ClassPool classPool = ClassPool.getDefault();
        try {
            classPool.importPackage("com.github.johnsonmoon.common.interceptor");
            CtClass ctClass = classPool.get(targetClass);
            String interceptorId = String.valueOf(interceptor.hashCode());
            DynamicInterceptorPool.put(interceptorId, interceptor);
            CtMethod ctMethod = ctClass.getDeclaredMethod(targetMethod);
            String beforeSrc = "$args = DynamicInterceptorPool.get(\"" + interceptorId + "\").before(\"" + injectionPoint + "\", $args);";
            ctMethod.insertBefore(beforeSrc);
            String afterSrc = "$_ = ($r)(DynamicInterceptorPool.get(\"" + interceptorId + "\").after(\"" + injectionPoint + "\", $args, $_));";
            ctMethod.insertAfter(afterSrc);
            ctClass.defrost();
            ctClass.toClass();
            logger.info("DynamicClassEnhanceInitializer enhance class {} succeeded.", targetClass);
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
            logger.info("DynamicClassEnhanceInitializer enhance class {} failed.", targetClass);
            return false;
        }
        return true;
    }
}

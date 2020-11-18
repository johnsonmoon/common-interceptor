package com.github.johnsonmoon.common.interceptor;

import com.github.johnsonmoon.common.interceptor.util.ClassUtils;
import javassist.*;
import javassist.bytecode.AttributeInfo;
import javassist.bytecode.CodeAttribute;
import javassist.bytecode.LocalVariableAttribute;
import javassist.bytecode.MethodInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Create by xuyh at 2020/2/12 21:52.
 */
public class InterceptorInitializer {
    private static Logger logger = LoggerFactory.getLogger(InterceptorInitializer.class);

    private static Map<String, CtClass> enhancedClassMap = new HashMap<>();

    public static void writeEnhancedClassFiles(String dir) {
        logger.info("Enhanced class file write begin...");
        int wrote = 0;
        if (!enhancedClassMap.isEmpty()) {
            for (Map.Entry<String, CtClass> entry : enhancedClassMap.entrySet()) {
                String className = entry.getKey();
                CtClass ctClass = entry.getValue();
                try {
                    ctClass.defrost();
                    ctClass.writeFile(dir);
                    logger.info("Class [{}] file write succeeded.", className);
                    wrote++;
                } catch (Exception e) {
                    logger.warn(e.getMessage(), e);
                }
            }
        }
        logger.info("Enhanced class file write finished. {} classe files wrote.", wrote);
    }

    public static void init(String interceptorPackage) {
        logger.info("Interceptor initialize, class enhancement begin...");
        List<String> names = ClassUtils.getAllClassNamesFromPackage(interceptorPackage);
        int loaded = 0;
        if (names != null && !names.isEmpty()) {
            Map<String, CtClass> ctClassMap = new HashMap<>();
            for (String name : names) {
                Class<?> clazz = null;
                try {
                    clazz = Class.forName(name);
                } catch (Exception e) {
                    logger.warn(e.getMessage(), e);
                }
                if (clazz == null) {
                    continue;
                }
                if (!(ClassUtils.isImplemented(clazz, Interceptor.class)
                        || ClassUtils.isImplemented(clazz, ResultInterceptor.class)
                        || ClassUtils.isImplemented(clazz, ParameterInterceptor.class))) {
                    continue;
                }
                Intercept intercept = ClassUtils.getIntercept(clazz);
                MethodCopy methodCopy = ClassUtils.getMethodCopy(clazz);
                ParameterIntercept parameterIntercept = ClassUtils.getParameterIntercept(clazz);
                if (intercept == null && methodCopy == null && parameterIntercept == null) {
                    continue;
                }
                if (intercept != null) {
                    if (!methodModify(intercept, clazz, ctClassMap)) {
                        continue;
                    }
                }
                if (methodCopy != null) {
                    if (!methodCopyAndModify(methodCopy, clazz, ctClassMap)) {
                        continue;
                    }
                }
                if (parameterIntercept != null) {
                    if (!parameterModify(parameterIntercept, clazz, ctClassMap)) {
                        continue;
                    }
                }
                loaded++;
            }
            if (!ctClassMap.isEmpty()) {
                for (Map.Entry<String, CtClass> entry : ctClassMap.entrySet()) {
                    String className = entry.getKey();
                    CtClass ctClass = entry.getValue();
                    try {
                        ctClass.toClass();
                        logger.info("Class [{}] enhance succeeded.", className);
                        enhancedClassMap.put(className, ctClass);
                    } catch (Exception e) {
                        logger.warn(e.getMessage(), e);
                    }
                }
            }
        }
        logger.info("Interceptor initialize, class enhancement finished. {} interceptors loaded.", loaded);
    }

    private static boolean methodModify(Intercept intercept, Class<?> clazz, Map<String, CtClass> ctClassMap) {
        String targetClass = intercept.targetClass();
        String targetMethod = intercept.targetMethod();
        boolean isConstructor = intercept.isConstructor();
        String[] constructorParameterTypes = intercept.constructorParameterTypes();
        if (targetClass.isEmpty()) {
            return false;
        }
        if (!isConstructor && targetMethod.isEmpty()) {
            return false;
        }
        ClassPool classPool = ClassPool.getDefault();
        if (ClassUtils.isImplemented(clazz, Interceptor.class)) {
            try {
                classPool.importPackage(ClassUtils.getPackage(Interceptor.class));
                classPool.importPackage(ClassUtils.getPackage(clazz));
                CtClass ctClass = classPool.get(targetClass);
                if (isConstructor) {
                    CtClass[] paramTypes = new CtClass[constructorParameterTypes.length];
                    if (constructorParameterTypes.length > 0) {
                        for (int i = 0; i < constructorParameterTypes.length; i++) {
                            String constructorParameterType = constructorParameterTypes[i];
                            try {
                                CtClass type = classPool.get(constructorParameterType);
                                paramTypes[i] = type;
                            } catch (Exception e) {
                                logger.warn(e.getMessage(), e);
                            }
                        }
                    }
                    CtConstructor ctConstructor = ctClass.getDeclaredConstructor(paramTypes);
                    ctConstructor.insertBefore("new " + ClassUtils.getShortName(clazz) + "().before($args);");
                    ctConstructor.insertAfter("new " + ClassUtils.getShortName(clazz) + "().after($args, $_);");
                    CtClass exceptionCtClass = classPool.get(Throwable.class.getCanonicalName());
                    ctConstructor.addCatch("{ new " + ClassUtils.getShortName(clazz) + "().afterThrow($args, $e); throw $e; }", exceptionCtClass);
                } else {
                    CtMethod ctMethod = ctClass.getDeclaredMethod(targetMethod);
                    ctMethod.insertBefore("new " + ClassUtils.getShortName(clazz) + "().before($args);");
                    ctMethod.insertAfter("new " + ClassUtils.getShortName(clazz) + "().after($args, $_);");
                    CtClass exceptionCtClass = classPool.get(Throwable.class.getCanonicalName());
                    ctMethod.addCatch("{ new " + ClassUtils.getShortName(clazz) + "().afterThrow($args, $e); throw $e; }", exceptionCtClass);
                }
                ctClassMap.put(targetClass, ctClass);
            } catch (Exception e) {
                if (e instanceof NotFoundException) {
                    logger.info("Class [{}] for interceptor [{}] not found, abort enhancement.", targetClass, ClassUtils.getShortName(clazz));
                } else {
                    logger.warn(e.getMessage(), e);
                }
                return false;
            }
        } else if (ClassUtils.isImplemented(clazz, ResultInterceptor.class)) {
            try {
                classPool.importPackage(ClassUtils.getPackage(Interceptor.class));
                classPool.importPackage(ClassUtils.getPackage(clazz));
                CtClass ctClass = classPool.get(targetClass);
                if (isConstructor) {
                    CtClass[] paramTypes = new CtClass[constructorParameterTypes.length];
                    if (constructorParameterTypes.length > 0) {
                        for (int i = 0; i < constructorParameterTypes.length; i++) {
                            String constructorParameterType = constructorParameterTypes[i];
                            try {
                                CtClass type = classPool.get(constructorParameterType);
                                paramTypes[i] = type;
                            } catch (Exception e) {
                                logger.warn(e.getMessage(), e);
                            }
                        }
                    }
                    CtConstructor ctConstructor = ctClass.getDeclaredConstructor(paramTypes);
                    ctConstructor.insertAfter("$_ = ($r)(new " + ClassUtils.getShortName(clazz) + "().after($args, $_));");
                } else {
                    CtMethod ctMethod = ctClass.getDeclaredMethod(targetMethod);
                    ctMethod.insertAfter("$_ = ($r)(new " + ClassUtils.getShortName(clazz) + "().after($args, $_));");
                }
                ctClassMap.put(targetClass, ctClass);
            } catch (Exception e) {
                if (e instanceof NotFoundException) {
                    logger.info("Class [{}] for interceptor [{}] not found, abort enhancement.", targetClass, ClassUtils.getShortName(clazz));
                } else {
                    logger.warn(e.getMessage(), e);
                }
                return false;
            }
        } else {
            return false;
        }
        return true;
    }

    private static boolean methodCopyAndModify(MethodCopy methodCopy, Class<?> clazz, Map<String, CtClass> ctClassMap) {
        String targetClass = methodCopy.targetClass();
        String targetMethod = methodCopy.targetMethod();
        if (targetClass.isEmpty() || targetMethod.isEmpty()) {
            return false;
        }
        String newMethodName = (methodCopy.newMethodName() == null || methodCopy.newMethodName().isEmpty()) ? targetMethod + "Copy" : methodCopy.newMethodName();
        ClassPool classPool = ClassPool.getDefault();
        if (ClassUtils.isImplemented(clazz, Interceptor.class)) {
            try {
                classPool.importPackage(ClassUtils.getPackage(Interceptor.class));
                classPool.importPackage(ClassUtils.getPackage(clazz));
                CtClass ctClass = classPool.get(targetClass);
                CtMethod originMethod = ctClass.getDeclaredMethod(targetMethod);
                List<AttributeInfo> originMethodAttributeInfoList = originMethod.getMethodInfo().getAttributes();
                CtMethod copyMethod = CtNewMethod.copy(originMethod, newMethodName, ctClass, null);
                if (originMethodAttributeInfoList != null) {
                    originMethodAttributeInfoList.forEach(attributeInfo -> {
                        if (attributeInfo instanceof CodeAttribute) {
                            return;
                        } else {
                            copyMethod.getMethodInfo().addAttribute(attributeInfo);
                        }
                    });
                }
                copyMethod.insertBefore("new " + ClassUtils.getShortName(clazz) + "().before($args);");
                copyMethod.insertAfter("new " + ClassUtils.getShortName(clazz) + "().after($args, $_);");
                CtClass exceptionCtClass = classPool.get(Throwable.class.getCanonicalName());
                copyMethod.addCatch("{ new " + ClassUtils.getShortName(clazz) + "().afterThrow($args, $e); throw $e; }", exceptionCtClass);
                ctClass.addMethod(copyMethod);
                ctClassMap.put(targetClass, ctClass);
            } catch (Exception e) {
                if (e instanceof NotFoundException) {
                    logger.info("Class [{}] for interceptor [{}] not found, abort enhancement.", targetClass, ClassUtils.getShortName(clazz));
                } else {
                    logger.warn(e.getMessage(), e);
                }
                return false;
            }
        } else if (ClassUtils.isImplemented(clazz, ResultInterceptor.class)) {
            try {
                classPool.importPackage(ClassUtils.getPackage(Interceptor.class));
                classPool.importPackage(ClassUtils.getPackage(clazz));
                CtClass ctClass = classPool.get(targetClass);
                CtMethod originMethod = ctClass.getDeclaredMethod(targetMethod);
                List<AttributeInfo> originMethodAttributeInfoList = originMethod.getMethodInfo().getAttributes();
                CtMethod copyMethod = CtNewMethod.copy(originMethod, newMethodName, ctClass, null);
                if (originMethodAttributeInfoList != null) {
                    originMethodAttributeInfoList.forEach(attributeInfo -> {
                        if (attributeInfo instanceof CodeAttribute) {
                            return;
                        } else {
                            copyMethod.getMethodInfo().addAttribute(attributeInfo);
                        }
                    });
                }
                copyMethod.insertAfter("$_ = ($r)(new " + ClassUtils.getShortName(clazz) + "().after($args, $_));");
                ctClass.addMethod(copyMethod);
                ctClassMap.put(targetClass, ctClass);
            } catch (Exception e) {
                if (e instanceof NotFoundException) {
                    logger.info("Class [{}] for interceptor [{}] not found, abort enhancement.", targetClass, ClassUtils.getShortName(clazz));
                } else {
                    logger.warn(e.getMessage(), e);
                }
                return false;
            }
        } else {
            return false;
        }
        return true;
    }

    private static boolean parameterModify(ParameterIntercept parameterIntercept, Class<?> clazz, Map<String, CtClass> ctClassMap) {
        int replacementParameterIndex = parameterIntercept.replacementParameterIndex();
        String parameterTypeName = parameterIntercept.replacementParameterTypeName();
        String targetClass = parameterIntercept.targetClass();
        String targetMethod = parameterIntercept.targetMethod();
        boolean isConstructor = parameterIntercept.isConstructor();
        String[] constructorParameterTypes = parameterIntercept.constructorParameterTypes();
        if (targetClass.isEmpty()) {
            return false;
        }
        if (!isConstructor && targetMethod.isEmpty()) {
            return false;
        }
        ClassPool classPool = ClassPool.getDefault();
        if (ClassUtils.isImplemented(clazz, ParameterInterceptor.class)) {
            try {
                String paramClassShortName = parameterTypeName.substring(parameterTypeName.lastIndexOf(".") + 1, parameterTypeName.length());
                String paramClassPackageName = parameterTypeName.substring(0, parameterTypeName.lastIndexOf("."));
                classPool.importPackage(paramClassPackageName);
                classPool.importPackage(ClassUtils.getPackage(Interceptor.class));
                classPool.importPackage(ClassUtils.getPackage(clazz));
                CtClass ctClass = classPool.get(targetClass);
                int index = replacementParameterIndex + 1;
                if (isConstructor) {
                    CtClass[] paramTypes = new CtClass[constructorParameterTypes.length];
                    if (constructorParameterTypes.length > 0) {
                        for (int i = 0; i < constructorParameterTypes.length; i++) {
                            String constructorParameterType = constructorParameterTypes[i];
                            try {
                                CtClass type = classPool.get(constructorParameterType);
                                paramTypes[i] = type;
                            } catch (Exception e) {
                                logger.warn(e.getMessage(), e);
                            }
                        }
                    }
                    CtConstructor ctConstructor = ctClass.getDeclaredConstructor(paramTypes);
                    ctConstructor.insertBefore("$" + index + " = (" + paramClassShortName + ")(new " + ClassUtils.getShortName(clazz) + "().before($args));");
                } else {
                    CtMethod ctMethod = ctClass.getDeclaredMethod(targetMethod);
                    ctMethod.insertBefore("$" + index + " = (" + paramClassShortName + ")(new " + ClassUtils.getShortName(clazz) + "().before($args));");
                }
                ctClassMap.put(targetClass, ctClass);
            } catch (Exception e) {
                if (e instanceof NotFoundException) {
                    logger.info("Class [{}] for interceptor [{}] not found, abort enhancement.", targetClass, ClassUtils.getShortName(clazz));
                } else {
                    logger.warn(e.getMessage(), e);
                }
                return false;
            }
        } else {
            return false;
        }
        return true;
    }
}

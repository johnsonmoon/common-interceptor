package com.github.johnsonmoon.test;

import com.github.johnsonmoon.common.interceptor.Interceptor;
import com.github.johnsonmoon.common.interceptor.ResultInterceptor;
import com.github.johnsonmoon.common.interceptor.util.ClassUtils;
import com.github.johnsonmoon.test.interceptor.TestResultInterceptor;
import com.github.johnsonmoon.test.obj.*;
import javassist.*;
import javassist.bytecode.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Create by xuyh at 2020/2/14 16:00.
 */
public class Test {
    private static Logger logger = LoggerFactory.getLogger(Test.class);

    public static void main(String[] args) throws Exception {
//        retValueModifyTest();
//        copyMethodTest();
//        copyMethodTestSrc();
//        noClassFoundInJvmTest();
//        constructorEnhancementTest();
//        parameterModifyTest();
        addFieldInterceptorTest();
    }

    private static void retValueModifyTest() throws Exception {
        ClassPool classPool = ClassPool.getDefault();
        classPool.importPackage(ClassUtils.getPackage(Interceptor.class));
        classPool.importPackage(ClassUtils.getPackage(TestResultInterceptor.class));
        CtClass ctClass = classPool.get(TestObjectRet.class.getCanonicalName());
        CtMethod ctMethod = ctClass.getDeclaredMethod("test");
        ctMethod.insertAfter("$_ = ($r)(new " + ClassUtils.getShortName(TestResultInterceptor.class) + "().after($args, $_));");
        ctClass.setName("TestObjectRet2");
        ctClass.writeFile(System.getProperty("user.dir"));
    }

    private static void copyMethodTest() throws Exception {
        ClassPool classPool = ClassPool.getDefault();
        CtClass ctClass = classPool.get(TestObjectCopy.class.getCanonicalName());
        CtMethod originMethod = ctClass.getDeclaredMethod("task");
        CtMethod copyMethod = CtNewMethod.copy(originMethod, originMethod.getName() + "Copy", ctClass, null);
        copyMethod.insertBefore("System.out.println(\"Hello!\");");
        List<AttributeInfo> attributeInfoList = originMethod.getMethodInfo().getAttributes();
        if (attributeInfoList != null) {
            for (AttributeInfo attributeInfo : attributeInfoList) {
                if (attributeInfo instanceof CodeAttribute) {//碰到 CodeAttribute 类型的属性，跳过，就不会拷贝一模一样的代码体了
                    continue;
                }
                copyMethod.getMethodInfo().addAttribute(attributeInfo);
            }
        }
        ctClass.addMethod(copyMethod);
        ctClass.setName("TestObjectCopy2");
        ctClass.writeFile(System.getProperty("user.dir"));
    }

    private static void copyMethodTestSrc() throws Exception {
        Class targetClass = TestObjectCopy.class;
        String methodName = "task";
        ClassPool classPool = ClassPool.getDefault();
        CtClass ctClass = classPool.get(targetClass.getCanonicalName());
        CtMethod originMethod = ctClass.getDeclaredMethod(methodName);
        String src = getMethodStructureStr(targetClass, methodName, "taskCopy");
        CtMethod newMethod = CtNewMethod.make(src, ctClass);
        newMethod.setBody(originMethod, null);
        newMethod.insertBefore("System.out.println(\"Hello!\");");
        List<AttributeInfo> attributeInfoList = originMethod.getMethodInfo().getAttributes();
        if (attributeInfoList != null) {
            for (AttributeInfo attributeInfo : attributeInfoList) {
                if (attributeInfo instanceof CodeAttribute) {//碰到 CodeAttribute 类型的属性，跳过，就不会拷贝一模一样的代码体了
                    continue;
                }
                newMethod.getMethodInfo().addAttribute(attributeInfo);
            }
        }
        ctClass.addMethod(newMethod);
        ctClass.setName("TestObjectCopy3");
        ctClass.writeFile(System.getProperty("user.dir"));
    }

    private static String getMethodStructureStr(Class<?> clazz, String methodName, String replaceMethodName) throws Exception {
        ClassPool classPool = ClassPool.getDefault();
        CtClass ctClass;
        ctClass = classPool.get(clazz.getName());
        Method method = null;
        for (Method methodLoop : clazz.getDeclaredMethods()) {
            if (methodLoop.getName().equalsIgnoreCase(methodName)) {
                method = methodLoop;
                break;
            }
        }
        if (method == null) {
            return "";
        }
        CtMethod ctMethod = ctClass.getDeclaredMethod(methodName);
        MethodInfo methodInfo = ctMethod.getMethodInfo();
        Class[] parameterTypes = method.getParameterTypes();
        CodeAttribute codeAttribute = methodInfo.getCodeAttribute();
        LocalVariableAttribute localVariableAttribute = (LocalVariableAttribute) codeAttribute.getAttribute(LocalVariableAttribute.tag);
        String[] paramNames = new String[ctMethod.getParameterTypes().length];
        int position = Modifier.isStatic(ctMethod.getModifiers()) ? 0 : 1;
        for (int i = 0; i < paramNames.length; i++) {
            paramNames[i] = localVariableAttribute.variableName(i + position);
        }
        StringBuilder structureStrBuilder = new StringBuilder();
        String modifiers = javassist.Modifier.toString(ctMethod.getModifiers());
        structureStrBuilder.append(modifiers);
        structureStrBuilder.append(" ");
        String retTypeSimpleName = method.getReturnType().getSimpleName();
        structureStrBuilder.append(retTypeSimpleName);
        structureStrBuilder.append(" ");
        structureStrBuilder.append(replaceMethodName == null ? methodName : replaceMethodName);
        structureStrBuilder.append("(");
        for (int j = 0; j < paramNames.length; j++) {
            structureStrBuilder.append(parameterTypes[j].getSimpleName());
            structureStrBuilder.append(" ");
            structureStrBuilder.append(paramNames[j]);
            structureStrBuilder.append(", ");
        }
        return structureStrBuilder.substring(0, structureStrBuilder.length() - 2) + "){}";
    }

    private static void noClassFoundInJvmTest() {
        ClassPool classPool = ClassPool.getDefault();
        try {
            CtClass ctClass = classPool.get("org.eclipse.jetty.server.handler.HandlerWrapper");
            CtMethod ctMethod = ctClass.getDeclaredMethod("setHandler");
            ctMethod.insertBefore("System.out.println(\"Hello!\");");
            ctClass.writeFile(System.getProperty("user.dir"));
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
        }
    }

    private static void constructorEnhancementTest() {
        ClassPool classPool = ClassPool.getDefault();
        try {
            CtClass ctClass = classPool.get("com.github.johnsonmoon.test.obj.TestObjectConstructor");
            CtClass param1 = classPool.get(AtomicLong.class.getCanonicalName());
            CtConstructor ctConstructor = ctClass.getDeclaredConstructor(new CtClass[]{param1});
            ctConstructor.insertBefore("System.out.println(\"Hello!\");");
            ctClass.writeFile(System.getProperty("user.dir"));
            ctClass.defrost();
            ctClass.toClass();


            TestObjectConstructor testObjectConstructor = new TestObjectConstructor(new AtomicLong(0));
            System.out.println(testObjectConstructor.getFlag());
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
        }
    }

    private static void parameterModifyTest() {
        ClassPool classPool = ClassPool.getDefault();
        try {
            CtClass ctClass = classPool.get("com.github.johnsonmoon.test.obj.TestParamModifyObject");

            CtClass[] paramTypes = new CtClass[]{classPool.get("java.lang.String"), classPool.get("java.lang.String")};

            CtConstructor constructor = ctClass.getDeclaredConstructor(paramTypes);
            //TODO
            constructor.insertBefore("$1 = (String)\"Hello!\";");
            constructor.insertBefore("$2 = (String)\"Hello!\";");

//            CtMethod method = ctClass.getDeclaredMethod("modifyField");
//            //TODO
//            method.insertBefore("$1 = (String)\"Hello!\";");
//            method.insertBefore("$2 = (String)\"Hello!\";");

//            CtMethod method1 = ctClass.getDeclaredMethod("setAbc");
//            //TODO
//            method1.insertBefore("$1 = (String)\"Hello!\";");


            ctClass.writeFile(System.getProperty("user.dir"));
            ctClass.defrost();
            ctClass.toClass();
            TestParamModifyObject testParamModifyObject = new TestParamModifyObject("abc", "def");
            //TODO
//            testParamModifyObject.modifyField("aabbcc", "ddeeff");
            //TODO
//            testParamModifyObject.setAbc("aabbcc");
            logger.info("{}", testParamModifyObject.toString());
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
        }
    }


    private static void addFieldInterceptorTest() {
        ClassPool classPool = ClassPool.getDefault();
        try {
            classPool.importPackage("com.github.johnsonmoon.test.obj");
            classPool.importPackage("com.github.johnsonmoon.common.interceptor");
            CtClass ctClass = classPool.get("com.github.johnsonmoon.test.obj.TestFieldObject");
            CtMethod method = ctClass.getDeclaredMethod("test");

            InterceptorPool.putInterceptor("abc", new ResultInterceptor() {
                @Override
                public Object after(Object[] params, Object retValue) {
                    return "Hello!";
                }
            });
            method.insertAfter("$_ = ($r)(InterceptorPool.getInterceptor(\"abc\").after($args, $_));");
            ctClass.writeFile(System.getProperty("user.dir"));
            ctClass.defrost();
            ctClass.toClass();
            logger.info(new TestFieldObject().test());
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
        }
    }
}

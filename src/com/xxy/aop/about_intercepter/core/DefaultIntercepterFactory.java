package com.xxy.aop.about_intercepter.core;

import com.xxy.aop.about_intercepter.Annotation.After;
import com.xxy.aop.about_intercepter.Annotation.Aspect;
import com.xxy.aop.about_intercepter.Annotation.Before;
import com.xxy.aop.about_intercepter.Annotation.ThrowException;
import com.xxy.beans.annotation.Bean;
import com.xxy.util.PackageScanner;

import java.lang.reflect.Method;

public class DefaultIntercepterFactory {
    private IntercepterFactory intercepterFactory;
    private final String BEFORE = "before";
    private final String AFTER = "after";
    private final String EXCEPTION = "exception";

    public DefaultIntercepterFactory() {
        this.intercepterFactory = new IntercepterFactory();
    }

    public void intercepterScan(String packagePath) {
        new PackageScanner() {
            @Override
            public void dealClass(Class<?> klass) {
                try {
                    doDealClass(klass);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.packageScanner(packagePath);
    }

    private void doDealClass(Class<?> klass) throws Exception {
        if(klass.isAnnotationPresent(Aspect.class)) {
            Object object = klass.newInstance();
            Method[] methods = klass.getMethods();
            for (Method method : methods) {
                dealMethod(method, object);
            }
        }
    }

    private void dealMethod(Method method, Object object) throws Exception {
        if(method.isAnnotationPresent(Before.class)) {
            if(!method.getReturnType().equals(boolean.class)) {
                return;
            }
            Before before = method.getAnnotation(Before.class);
            Class<?> targetClass = before.klass();
            String targetMethodName = before.method();
            Class<?>[] parasType = before.parasType();
            doDealMethod(object, method, targetClass, targetMethodName, BEFORE, parasType);
        } else if(method.isAnnotationPresent(After.class)) {
            if(!method.getReturnType().equals(Object.class)) {
                return;
            }
            After after = method.getAnnotation(After.class);
            Class<?> targetClass = after.klass();
            String methodName = after.method();
            Class<?>[] parasType = after.parasType();
            doDealMethod(object, method, targetClass, methodName, AFTER, parasType);
        } else if(method.isAnnotationPresent(ThrowException.class)) {
            if(!method.getReturnType().equals(void.class)) {
                return;
            }
            ThrowException throwException = method.getAnnotation(ThrowException.class);
            Class<?> targetClass = throwException.klass();
            String methodName = throwException.method();
            Class<?>[] parasType = throwException.parasType();
            doDealMethod(object, method, targetClass, methodName, EXCEPTION, parasType);
        }
    }

    private void doDealMethod(Object object, Method method, Class<?> targetClass, String targetMethodName, String type,
                              Class<?>[] parasType) throws Exception {
        try {
            Method targetMethod = targetClass.getMethod(targetMethodName, parasType);
            IntercepterTargetDefinition intercepterTargetDefinition =
                    new IntercepterTargetDefinition(targetClass, targetMethod);
            IntercepterMethodDefinition intercepterMethodDefinition =
                    new IntercepterMethodDefinition(targetClass, method, object);
            if(type.equals(BEFORE)) {
                intercepterFactory.addBeforeIntercepter(intercepterTargetDefinition, intercepterMethodDefinition);
            } else if(type.equals(AFTER)) {
                intercepterFactory.addAfterIntercepter(intercepterTargetDefinition, intercepterMethodDefinition);
            } else if(type.equals(EXCEPTION)) {
                intercepterFactory.addExceptionIntercepter(intercepterTargetDefinition, intercepterMethodDefinition);
            }
        } catch (NoSuchMethodException e) {
            throw new NoSuchMethodException("找不到对应的方法" + targetMethodName +
                    "(" + method.getParameterTypes() + ")");
        }
    }
}

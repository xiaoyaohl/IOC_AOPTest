package com.xxy.aop.about_intercepter.core;

import java.lang.reflect.Method;

public class IntercepterMethodDefinition {
    private Class<?> klass;
    private Method method;
    private Object object;

    public IntercepterMethodDefinition(Class<?> klass, Method method, Object object) {
        this.klass = klass;
        this.method = method;
        this.object = object;
    }

    public Class<?> getKlass() {
        return klass;
    }

    public void setKlass(Class<?> klass) {
        this.klass = klass;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }
}

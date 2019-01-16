package com.xxy.rmi.core;

import java.lang.reflect.Method;

public class RMIMethodDefinition {
    private Class<?> klass;
    private Method method;

    public RMIMethodDefinition(Class<?> klass, Method method) {
        this.klass = klass;
        this.method = method;
    }

    public RMIMethodDefinition() {
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
}

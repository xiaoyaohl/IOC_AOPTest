package com.xxy.aop.about_intercepter.core;

import java.lang.reflect.Method;
import java.util.Objects;

public class IntercepterTargetDefinition {
    private Class<?> klass;
    private Method method;

    public IntercepterTargetDefinition(Class<?> klass, Method method) {
        this.klass = klass;
        this.method = method;
    }

    public Class<?> getKlass() {
        return klass;
    }

    public Method getMethod() {
        return method;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IntercepterTargetDefinition that = (IntercepterTargetDefinition) o;
        return Objects.equals(klass, that.klass) &&
                Objects.equals(method, that.method);
    }

    @Override
    public int hashCode() {
        return Objects.hash(klass, method);
    }
}

package com.xxy.aop.about_intercepter.core;

import com.xxy.aop.about_intercepter.interfaces.IIntercepterFactory;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

//拦截器工厂，对指定方法添加拦截器
public class IntercepterFactory implements IIntercepterFactory {
    private static final Map<IntercepterTargetDefinition, List<IntercepterMethodDefinition>> beforeMap;
    private static final Map<IntercepterTargetDefinition, List<IntercepterMethodDefinition>> afterMap;
    private static final Map<IntercepterTargetDefinition, List<IntercepterMethodDefinition>> exceptionMap;

    static {
        beforeMap = new ConcurrentHashMap<>();
        afterMap = new ConcurrentHashMap<>();
        exceptionMap = new ConcurrentHashMap<>();
    }

    private void addIntercepter(Map<IntercepterTargetDefinition, List<IntercepterMethodDefinition>> map,
                                IntercepterTargetDefinition interTarget,
                                IntercepterMethodDefinition interMethod) {
        List<IntercepterMethodDefinition> methodList = map.get(interTarget);
        if (methodList == null) {
            methodList = new LinkedList<>();
            map.put(interTarget, methodList);
        }
        if(interMethod.getKlass().equals(interTarget.getKlass())) {
            methodList.add(interMethod);
        }
    }

    @Override
    public void addBeforeIntercepter(IntercepterTargetDefinition interTarget, IntercepterMethodDefinition interMethod) {
        addIntercepter(beforeMap, interTarget, interMethod);
    }

    @Override
    public void addAfterIntercepter(IntercepterTargetDefinition interTarget, IntercepterMethodDefinition interMethod) {
        addIntercepter(afterMap, interTarget, interMethod);
    }

    @Override
    public void addExceptionIntercepter(IntercepterTargetDefinition interTarget, IntercepterMethodDefinition interMethod) {
        addIntercepter(exceptionMap, interTarget, interMethod);
    }

    @Override
    public List<IntercepterMethodDefinition> getBeforeList(IntercepterTargetDefinition interceTarget) {
        return beforeMap.get(interceTarget);
    }

    @Override
    public List<IntercepterMethodDefinition> getAfterList(IntercepterTargetDefinition interceTarget) {
        return afterMap.get(interceTarget);
    }

    @Override
    public List<IntercepterMethodDefinition> getExceptionList(IntercepterTargetDefinition interceTarget) {
        return exceptionMap.get(interceTarget);
    }

}

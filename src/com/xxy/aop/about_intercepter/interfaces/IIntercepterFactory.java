package com.xxy.aop.about_intercepter.interfaces;

import com.xxy.aop.about_intercepter.core.IntercepterMethodDefinition;
import com.xxy.aop.about_intercepter.core.IntercepterTargetDefinition;

import java.util.List;

public interface IIntercepterFactory {
    void addBeforeIntercepter(IntercepterTargetDefinition interTarget,
                         IntercepterMethodDefinition interMethod);
    void addAfterIntercepter(IntercepterTargetDefinition interTarget,
                              IntercepterMethodDefinition interMethod);
    void addExceptionIntercepter(IntercepterTargetDefinition interTarget,
                              IntercepterMethodDefinition interMethod);

    List<IntercepterMethodDefinition> getBeforeList(IntercepterTargetDefinition interceTarget);
    List<IntercepterMethodDefinition> getAfterList(IntercepterTargetDefinition interceTarget);
    List<IntercepterMethodDefinition> getExceptionList(IntercepterTargetDefinition interceTarget);
}

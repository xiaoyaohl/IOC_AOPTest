package com.xxy.test.intercepter;

import com.xxy.aop.about_intercepter.Annotation.After;
import com.xxy.aop.about_intercepter.Annotation.Aspect;
import com.xxy.aop.about_intercepter.Annotation.Before;
import com.xxy.test.someClasses.A;

@Aspect
public class AIntercepter {
    @Before(klass = A.class, method = "fun")
    public boolean aBeforeIntercepter() {
        System.out.println("这是A的前置拦截!");
        return true;
    }

    @After(klass = A.class, method = "fun")
    public Object aAfterIntercepter(Object object) {
        System.out.println("这是A的后置拦截!");
        return object;
    }
}

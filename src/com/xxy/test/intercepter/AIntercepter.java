package com.xxy.test.intercepter;

import com.xxy.aop.about_intercepter.Annotation.After;
import com.xxy.aop.about_intercepter.Annotation.Aspect;
import com.xxy.aop.about_intercepter.Annotation.Before;
import com.xxy.test.someClasses.A;
import com.xxy.test.someClasses.B;

@Aspect
public class AIntercepter {
    @Before(klass = A.class, method = "fun", parasType = {B.class})
    public boolean aBeforeIntercepter(B b) {
        System.out.println("传入的b中的num的值：" + b.getNum());
        b.setNum(123);
        System.out.println("这是A的前置拦截!");
        return true;
    }

    @After(klass = A.class, method = "fun", parasType = {B.class})
    public Object aAfterIntercepter(Object object) {
        System.out.println("这是A的后置拦截!");
        return object;
    }
}

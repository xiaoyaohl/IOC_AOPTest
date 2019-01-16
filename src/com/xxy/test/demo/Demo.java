package com.xxy.test.demo;

import com.xxy.aop.about_intercepter.core.DefaultIntercepterFactory;
import com.xxy.beans.Factory.DefaultBeanFactory;
import com.xxy.test.someClasses.A;
import com.xxy.test.someClasses.B;
import com.xxy.test.someClasses.D;

public class Demo {
    public static void main(String[] args) {
        DefaultBeanFactory defaultBeanFactory = new DefaultBeanFactory();
        defaultBeanFactory.packageScan("com.xxy.test");
        DefaultIntercepterFactory defaultIntercepterFactory = new DefaultIntercepterFactory();
        defaultIntercepterFactory.intercepterScan("com.xxy.test");
        B b = new B();
        b.setNum(0);
        A a = defaultBeanFactory.getproxy(A.class);
        a.fun(b);
        System.out.println(b.getNum());
        D d = defaultBeanFactory.getproxy(D.class);
        System.out.println(a);
        System.out.println(d);
    }
}

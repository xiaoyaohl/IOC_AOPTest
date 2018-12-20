package com.xxy.test.demo;

import com.xxy.aop.about_intercepter.core.DefaultIntercepterFactory;
import com.xxy.beans.Factory.DefaultBeanFactory;
import com.xxy.test.someClasses.A;
import com.xxy.test.someClasses.B;

public class Demo {
    public static void main(String[] args) {
        DefaultBeanFactory defaultBeanFactory = new DefaultBeanFactory();
        defaultBeanFactory.packageScan("com.xxy.test");
        DefaultIntercepterFactory defaultIntercepterFactory = new DefaultIntercepterFactory();
        defaultIntercepterFactory.intercepterScan("com.xxy.test");
        A a = defaultBeanFactory.getproxy(A.class);
        System.out.println(a);
    }
}

package com.xxy.test.someClasses;

import com.xxy.beans.annotation.Autowired;
import com.xxy.beans.annotation.Component;

import java.sql.SQLOutput;

@Component
public class A {
    @Autowired
    private B b;

    public A() {
    }

    public void setB(B b) {
        this.b = b;
    }

    public B getB() {
        return b;
    }

    public void fun(B b) {
//        System.out.println("自动注入的num：" + this.num);
        System.out.println("这是fun方法");
    }

    @Override
    public String toString() {
        return "这是A - >B\n" + b;
    }
}

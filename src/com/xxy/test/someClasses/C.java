package com.xxy.test.someClasses;

import com.xxy.beans.annotation.Autowired;
import com.xxy.beans.annotation.Component;

@Component
public class C {
    @Autowired
    private A a;

    public C() {
    }

    public void setA(A a) {
        this.a = a;
    }

    public void fun() {

    }

    @Override
    public String toString() {
        return "这是C - >A" + a;
    }
}

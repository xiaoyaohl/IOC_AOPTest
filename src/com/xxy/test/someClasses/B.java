package com.xxy.test.someClasses;

import com.xxy.beans.annotation.Autowired;
import com.xxy.beans.annotation.Component;

@Component
public class B {
    @Autowired
    private C c;

    public B() {
    }

    public void setC(C c) {
        this.c = c;
    }

    @Override
    public String toString() {
        return "这是B - >C\n" + c;
    }
}

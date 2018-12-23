package com.xxy.test.someClasses;

import com.xxy.beans.annotation.Autowired;
import com.xxy.beans.annotation.Bean;
import com.xxy.beans.annotation.Component;

@Component
public class B {
    @Autowired
    private C c;
    @Autowired(value = "123")
    private int num;

    public B() {
    }

    public void setC(C c) {
        this.c = c;
    }

    public C getC() {
        return c;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    @Bean
    public D autowiredD() {
        return new D();
    }

    @Override
    public String toString() {
        return "这是B - >C\n" + c;
    }
}

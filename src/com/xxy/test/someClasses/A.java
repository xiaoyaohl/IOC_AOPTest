package com.xxy.test.someClasses;

import com.xxy.beans.annotation.Autowired;
import com.xxy.beans.annotation.Component;

@Component
public class A {
    @Autowired
    private B b;
    private volatile static int count = 0;
    public A() {
    }
    public void setB(B b) {
        this.b = b;
    }
    public void fun() {
        System.out.println("这是fun方法");
    }
    @Override
    public String toString() {
        if(++count >= 2) {
            try {
                System.out.println(count);
                Thread.sleep(1000000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println( "return 前：" + count);
        return "这是A - >B\n" + b;
    }
}

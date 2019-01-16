package com.xxy.rmi.test;

public class Test implements ITest {
    @Override
    public String fun() {
        System.out.println("本地方法并不执行!");
        return null;
    }
}

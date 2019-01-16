package com.xxy.rmi.test;

import com.xxy.rmi.Annotation.RMIClass;
import com.xxy.rmi.Annotation.RMIMethod;

@RMIClass
public class Demo implements ITest {

    @Override
    @RMIMethod
    public String fun() {
        System.out.println("远程调用！");
        return "远程调用返回";
    }
}

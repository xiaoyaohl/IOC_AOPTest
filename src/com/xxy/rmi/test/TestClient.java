package com.xxy.rmi.test;

import com.google.gson.GsonBuilder;
import com.xxy.rmi.core.RMIProxy;

public class TestClient {
    public static void main(String[] args) {
        ITest test = new RMIProxy("192.168.74.1", 54199, new GsonBuilder().create())
                .getRPCPRoxy(ITest.class);
        System.out.println(test.fun());
    }
}

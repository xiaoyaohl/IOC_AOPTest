package com.xxy.rmi.test;

import com.xxy.rmi.core.RMIServer;

public class TestServer {
    public static void main(String[] args) {
        RMIServer rmiServer = new RMIServer(54199);
        rmiServer.startup("com.xxy.rmi.test");
    }
}

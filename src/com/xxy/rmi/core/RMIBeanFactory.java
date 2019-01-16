package com.xxy.rmi.core;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RMIBeanFactory {
    private static Map<Integer, RMIMethodDefinition> RPCMethodMap;

    static {
        RPCMethodMap = new ConcurrentHashMap<>();
    }

    public RMIBeanFactory() {
    }

    static void addRpcMethod(int id, RMIMethodDefinition method) {
        RPCMethodMap.remove(id);
        RPCMethodMap.put(id, method);
    }

    static  RMIMethodDefinition getRPCMethod(int id) {
        RMIMethodDefinition method = RPCMethodMap.get(id);
        if (method == null) {
//            TODO 抛出异常
            return method;
        }

        return method;
    }
}

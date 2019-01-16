package com.xxy.rmi.core;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RMIFactory {
    private Map<Integer, Method> RpcInterfacesMap;

    public RMIFactory() {
        RpcInterfacesMap = new ConcurrentHashMap<>();
    }

    void addInetrface(int id, Method method) {
        this.RpcInterfacesMap.remove(id);
        this.RpcInterfacesMap.put(id, method);
    }

    Method getRPCMethod(int id) {
        Method method = RpcInterfacesMap.get(id);
        if(method == null) {
//            TODO 抛出异常，找不到相关方法！
            return null;
        }

        return method;
    }
}

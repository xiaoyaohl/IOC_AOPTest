package com.xxy.rmi.core;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.Socket;

public class RMIProxy {
    private String RPCip;
    private int RPCport;
    private Socket socket;
    private Gson gson;
    private ObjectInputStream ois;
    private ObjectOutputStream oos;

    public RMIProxy() {
    }

    public RMIProxy(String RPCip, int RPCport, Gson gson) {
        this.RPCip = RPCip;
        this.RPCport = RPCport;
        this.gson = gson;
    }

    public RMIProxy(Socket socket, Gson gson) {
        this.socket = socket;
        this.gson = gson;
    }

    public <T> T getRPCPRoxy(Class<?> klass) {
        return JDKProxy(klass, null);
    }

    public <T> T getRPCPRoxy(Object object) {
        return JDKProxy(object.getClass(), object);
    }

    private <T> T JDKProxy(Class<?> klass, Object object) {
        Class<?>[] classes = {klass};
        InvocationHandler invocationHandler = new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                return doInvoke(object, args, method);
            }
        };
        return (T) Proxy.newProxyInstance(klass.getClassLoader(), classes, invocationHandler);
    }

    private Object doInvoke(Object object, Object[] args, Method method) throws IOException {
        Class<?> returnType = method.getReturnType();
        if(socket == null && RPCip != null && RPCport >= 0) {
            this.socket = new Socket(RPCip, RPCport);
        }
        String str = method.getName();
        int id = str.hashCode();
        oos = new ObjectOutputStream(socket.getOutputStream());
        oos.writeInt(id);
        oos.writeObject(args);
        ois = new ObjectInputStream(socket.getInputStream());
        Object result = null;
        try {
            result = ois.readObject();
            close();
            return result;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }

    private void close() {
        if (this.ois != null) {
            try {
                ois.close();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                ois = null;
            }
        }

        if (this.oos != null) {
            try {
                oos.close();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                oos = null;
            }
        }

        if (this.socket != null || !this.socket.isClosed()) {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                socket = null;
            }
        }
    }

    public RMIProxy setRPCip(String RPCip) {
        this.RPCip = RPCip;
        return this;
    }

    public RMIProxy setRPCport(int RPCport) {
        this.RPCport = RPCport;
        return this;
    }

    public RMIProxy setSocket(Socket socket) {
        this.socket = socket;
        return this;
    }

    public RMIProxy setGson(Gson gson) {
        this.gson = gson;
        return this;
    }
}

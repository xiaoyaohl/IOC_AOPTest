package com.xxy.rmi.core;

import com.xxy.rmi.Annotation.RMIClass;
import com.xxy.rmi.Annotation.RMIMethod;
import com.xxy.util.PackageScanner;

import java.io.IOException;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;

public class RMIServer implements Runnable {
    private int port;
    private ServerSocket serverSocket;
    private volatile boolean connecctionLinsting;
    private volatile int num;

    public RMIServer() {
    }

    public RMIServer(int port) {
        this.port = port;
    }

    private void initFactory(String packagePath) {
        new PackageScanner() {
            @Override
            public void dealClass(Class<?> klass) {
                Class<?>[] interfaces = klass.getInterfaces();
                if(!klass.isAnnotationPresent(RMIClass.class) || interfaces.length <= 0) {
                    return;
                }
                Method[] methods = klass.getDeclaredMethods();
                for (Method method : methods) {
                    if (method.isAnnotationPresent(RMIMethod.class)) {
                        String str = method.getName();
                        int id = str.hashCode();
                        RMIBeanFactory.addRpcMethod(id, new RMIMethodDefinition(klass, method));
                    }
                }
            }
        }.packageScanner(packagePath);
    }

    public void startup(String packagePath) {
        initFactory(packagePath);
        try {
            serverSocket = new ServerSocket(port);
            try {
                synchronized (RMIServer.class) {
                    new Thread(this, "RPC_Server").start();
                    connecctionLinsting = true;
                    RMIServer.class.wait();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stopServer() {
        if(this.serverSocket != null) {
            connecctionLinsting = false;
            try {
                this.serverSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                this.serverSocket = null;
            }
        }
    }

    public RMIServer setPort(int port) {
        this.port = port;
        return this;
    }

    @Override
    public void run() {
        synchronized (RMIServer.class) {
            RMIServer.class.notify();
        }
//        System.out.println("开始侦听！");
        while (connecctionLinsting) {
            try {
                Socket socket = serverSocket.accept();
                new RMIExecutor(socket);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

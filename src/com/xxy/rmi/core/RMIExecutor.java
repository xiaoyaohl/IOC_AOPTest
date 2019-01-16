package com.xxy.rmi.core;

import com.sun.xml.internal.bind.v2.schemagen.episode.Klass;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.Socket;

public class RMIExecutor implements Runnable {
    private Socket socket;
    private ObjectInputStream ois;
    private ObjectOutputStream oos;

    public RMIExecutor() {
    }

    public RMIExecutor(Socket socket) {
        this.socket = socket;
        synchronized (RMIExecutor.class) {
            new Thread(this::run).start();
            try {
                RMIExecutor.class.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
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

    @Override
    public void run() {
        synchronized (RMIExecutor.class) {
            RMIExecutor.class.notify();
        }
        try {
            this.ois = new ObjectInputStream(socket.getInputStream());
            int id = ois.readInt();
            Object[] args = (Object[]) ois.readObject();
            RMIMethodDefinition rmiMethodDefinition = RMIBeanFactory.getRPCMethod(id);
            Class<?> klass = rmiMethodDefinition.getKlass();
            Object object = klass.newInstance();
            Object result = rmiMethodDefinition.getMethod().invoke(object, args);
            oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject(result);
            close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {


        }
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }
}

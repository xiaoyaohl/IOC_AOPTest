package com.xxy.beans.core;

import com.xxy.beans.interfaces.IBeanDefinitionHolder;

import java.util.ArrayList;
import java.util.List;

public class BeanDefinitionHolder <T> implements IBeanDefinitionHolder {
//    beanDefinition的持有者
    private BeanDefinition beanDefinition;
    private Class<?> beanClass;
    private String beanName;
    private T proxy;
    private volatile boolean injection;
//    TODO 别名数组，暂时不想做
    private List<String> alias;
//实例化beanDefiniyionHolder时必须传入BeanDefinition以及beanClass或者className
    public BeanDefinitionHolder(BeanDefinition beanDefinition, Class<?> beanClass) {
        this.beanDefinition = beanDefinition;
        this.beanClass = beanClass;
        this.beanName = beanClass.getName();
        this.injection = false;
        alias = new ArrayList<>();
    }

    public BeanDefinitionHolder(BeanDefinition beanDefinition, String beanName) {
        this.beanDefinition = beanDefinition;
        this.beanName = beanName;
    }

    public Object getProxy() {
        return this.proxy;
    }

    public void setProxy(T proxy) {
        this.proxy = proxy;
    }

    @Override
    public Class<?> getBeanClass() {
        if(this.beanClass != null) {
            return this.beanClass;
        } else if(this.beanName != null) {
            try {
                return Class.forName(beanName);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    public String getBeanName() {
        if(this.beanName != null) {
            return this.beanName;
        } else {
            return this.beanClass.getName();
        }
    }

    @Override
    public Object getObject() {
        return this.beanDefinition.getObject();
    }

    @Override
    public boolean isInjection() {
        return this.injection;
    }

    @Override
    public void changeInjection() {
        this.injection = true;
    }

    @Override
    public String toString() {
        return "BeanDefinitionHolder{" +
                "beanName='" + beanName + '\'' +
                '}';
    }
}

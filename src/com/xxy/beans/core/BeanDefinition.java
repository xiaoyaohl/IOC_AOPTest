package com.xxy.beans.core;

import com.xxy.beans.interfaces.IBeanDefinition;

public class BeanDefinition implements IBeanDefinition {
    private Object object;
    private String beanName;
//    TODO 是否延迟加载，即懒汉模式，在getBean（）的时候在执行
//    在这里先不实现，如果不存在依赖关系，则全部延迟加载
    private String scope;

    public BeanDefinition() {
    }

    public BeanDefinition(Object object) {
        this.object = object;
    }

    @Override
    public Object getObject() {
        return this.object;
    }

    @Override
    public void steObject(Object object) {
        this.object = object;
    }

    @Override
    public void setBeanName(String beanName) {
        this.beanName = beanName;
    }

    @Override
    public String getBeanName() {
        return this.beanName;
    }
}

package com.xxy.beans.interfaces;

public interface IBeanDefinitionHolder {
    Class<?> getBeanClass();
    String getBeanName();
    Object getObject();

    boolean isInjection();
    void changeInjection();
}

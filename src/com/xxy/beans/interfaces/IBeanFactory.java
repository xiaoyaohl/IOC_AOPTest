package com.xxy.beans.interfaces;

import com.xxy.beans.core.BeanDefinitionHolder;

public interface IBeanFactory {
    <T> T createCGLProxy(BeanDefinitionHolder beanDefinitionHolder);
    <T> T cteateJDKProxy(BeanDefinitionHolder beanDefinitionHolder);
    void setBeanDefinitionHolder(BeanDefinitionHolder beanDefinitionHolder);
    BeanDefinitionHolder getBeanDefinitionHolder(String beanName);
    <T> T getBean(String name) throws Exception;
    <T> T createBean(BeanDefinitionHolder beanDefinitionHolder) throws Exception;
}

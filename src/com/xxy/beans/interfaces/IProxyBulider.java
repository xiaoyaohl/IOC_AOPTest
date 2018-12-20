package com.xxy.beans.interfaces;

import com.xxy.beans.core.BeanDefinition;
import com.xxy.beans.core.BeanDefinitionHolder;

public interface IProxyBulider {
//    返回BeanDefinition
    BeanDefinitionHolder getBeanDefinitionBeanDefinition();
//     返回cglib代理
    <T> T getCGLProxy(BeanDefinitionHolder beanDefinitionHolder);
    <T> T getJDKProxy(BeanDefinitionHolder beanDefinitionHolder);

    <T> T getProxy();
}

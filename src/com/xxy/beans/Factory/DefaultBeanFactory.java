package com.xxy.beans.Factory;

import com.xxy.beans.annotation.Component;
import com.xxy.beans.core.BeanDefinition;
import com.xxy.beans.core.BeanDefinitionHolder;
import com.xxy.beans.interfaces.IBeanBuilder;
import com.xxy.util.PackageScanner;

public class DefaultBeanFactory extends BeanFactory implements IBeanBuilder {

    public void packageScan(String packagePath) {
        new PackageScanner() {

            @Override
            public void dealClass(Class<?> klass) {
                dealKlass(klass);
            }

        }.packageScanner(packagePath);
    }

    private void dealKlass(Class<?> klass) {
        if(!klass.isAnnotationPresent(Component.class)) {
            return;
        }
        Component component = klass.getAnnotation(Component.class);
        String alias = component.name();
//      为了满足远程方法调用，这里应该优先使用jdk创建代理，而且Spring也优先使用jdk创建代理
//      为了方便初始化，这里统一使用无参构造，对于初始化时需要传入的参数等，使用对成员进行注入的方法
//      这里先进行包扫描形成对应的beanDefinitionMap，为以后getBean时取得Bean的所有信息打好基础
//      Spring中对于BeanDefinition的定义复杂，这里做的是极为简陋的
//       直接使用无参构造及逆行初始化
//       TODO 全部采取懒汉模式所以这里不用进行实例化操作，以后应该加上饿汉模式，拦截器是通过相关的代理加上的，
//       在形成代理时是否已经有了相应的拦截器链并不重要,但是在程序启动之前必须对拦截器类进行扫描
        addBeanDefinitionholder(klass);
        if(alias.length() > 0) {
            addAlias(klass.getName(), alias);
        }
    }

    @Override
    public Object getObject(Class<?> klass) {
        return getBeanDefinitionHolder(klass.getName()).getObject();
    }

    @Override
    public <T> T getproxy(Class<?> klass) {
        try {
            return getBean(klass.getName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void addBeanDefinitionholder(Class<?> klass) {
        BeanDefinition beanDefinition = new BeanDefinition();
        try {
            Object object = klass.newInstance();
            beanDefinition.steObject(object);
            beanDefinition.setBeanName(klass.getName());
            BeanDefinitionHolder beanDefinitionHolder = new BeanDefinitionHolder(beanDefinition, klass);
            setBeanDefinitionHolder(beanDefinitionHolder);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}

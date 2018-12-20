package com.xxy.beans.core;

import com.xxy.aop.about_intercepter.core.IntercepterFactory;
import com.xxy.aop.about_intercepter.core.IntercepterMethodDefinition;
import com.xxy.aop.about_intercepter.core.IntercepterTargetDefinition;
import com.xxy.beans.interfaces.IProxyBulider;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.List;

public class ProxyBulider <T> implements IProxyBulider {
    private BeanDefinitionHolder beanDefinitionHolder;
    private T proxyObject;
//     必须实例化一个BeanDefinitionHolder并传入
    public ProxyBulider(BeanDefinitionHolder beanDefinitionHolder) {
        this.beanDefinitionHolder = beanDefinitionHolder;
    }

    @Override
    public BeanDefinitionHolder getBeanDefinitionBeanDefinition() {
        return this.beanDefinitionHolder;
    }

    @Override
    public <T> T getCGLProxy(BeanDefinitionHolder beanDefinitionHolder) {
       this.proxyObject = cjlProxy(beanDefinitionHolder.getObject(), beanDefinitionHolder.getBeanClass());
        return (T) proxyObject;
    }

    @Override
    public <T> T getJDKProxy(BeanDefinitionHolder beanDefinitionHolder) {
        this.proxyObject = jdkProxy(beanDefinitionHolder.getObject(), beanDefinitionHolder.getBeanClass());
        return (T) proxyObject;
    }

    @Override
    public <T> T getProxy() {
        return (T) this.proxyObject;
    }

    private <T> T jdkProxy(Object object, Class<?> klass) {
        InvocationHandler invocationHandler = new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                return doInvok(klass, object, method, args);
            }
        };
        return (T) Proxy.newProxyInstance(klass.getClassLoader(), klass.getDeclaredClasses(), invocationHandler);
    }

    private <T> T cjlProxy(Object object, Class<?> klass) {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(klass);
        enhancer.setCallback(new MethodInterceptor() {
            @Override
            public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxyObject) throws Throwable {
                return doInvok(klass, object, method, args);
            }
        });
        return (T) enhancer.create();
    }

//    如有对应的拦截器链，则执行相应的拦截器，若没有,则直接执行对应的方法
    private Object doInvok(Class<?> klass, Object object, Method method, Object[] args) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        Object result = null;
        IntercepterFactory intercepterFactory = new IntercepterFactory();
        IntercepterTargetDefinition itd = new IntercepterTargetDefinition(klass, method);
        List<IntercepterMethodDefinition> interBeforeList = intercepterFactory.getBeforeList(itd);
        List<IntercepterMethodDefinition> interAfterList = intercepterFactory.getAfterList(itd);
        List<IntercepterMethodDefinition> interExceptionList = intercepterFactory.getExceptionList(itd);
        if(interBeforeList != null) {
            for(IntercepterMethodDefinition imd : interBeforeList) {
                if((boolean)imd.getMethod().invoke(imd.getObject(), args) == false) {
                    return result;
                }
            }
        }
        try {
            result =  method.invoke(object, args);
            if(interAfterList == null) {
                return result;
            } else {
                for(IntercepterMethodDefinition imd : interAfterList) {
                    result = imd.getMethod().invoke(imd.getObject(), result);
                }
            }

        } catch(Throwable e) {
            if(interExceptionList != null) {
                for(IntercepterMethodDefinition imd : interExceptionList) {
                    imd.getMethod().invoke(object, e);
                }
            }

            throw e;
        }

        return result;
    }
}

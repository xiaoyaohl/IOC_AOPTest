package com.xxy.beans.Factory;

import com.xxy.beans.annotation.Autowired;
import com.xxy.beans.annotation.Bean;
import com.xxy.beans.core.BeanDefinition;
import com.xxy.beans.core.BeanDefinitionHolder;
import com.xxy.beans.core.ProxyBulider;
import com.xxy.beans.exceptions.BeanDefinitionHolderIsNotExistException;
import com.xxy.beans.exceptions.BeanMethodReturnTypeIsErrorException;
import com.xxy.beans.exceptions.PrimitiveMustHaveValueAttributeException;
import com.xxy.beans.interfaces.IBeanFactory;
import com.xxy.utils.BeanUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class BeanFactory implements IBeanFactory {
//    这里的beanMap存储的全是代理
    private volatile static Map<String, Object> beanMap;
    private volatile static Map<String, String> aliasMap;
    private volatile static Map<String, BeanDefinitionHolder> beanDefinitionHolderMap;
    private final static Object object = new Object();
    private final static Object createBeanObject = new Object();

    static {
        beanMap = new ConcurrentHashMap<>();
        aliasMap = new ConcurrentHashMap<>();
        beanDefinitionHolderMap = new ConcurrentHashMap<>();
    }

    private <T> T cglProxy(BeanDefinitionHolder beanDefinitionHolder) {
        String beanName = beanDefinitionHolder.getBeanName();
        T proxy = (T)beanMap.get(beanName);
        if(proxy != null) {
            return proxy;
        }
        synchronized (object) {
            proxy = (T)beanMap.get(beanName);
            if(proxy == null) {
                ProxyBulider proxyBulider = new ProxyBulider(beanDefinitionHolder);
                proxy = (T) proxyBulider.getCGLProxy(beanDefinitionHolder);
            }
        }
        return (T) proxy;
    }

    private <T> T jdkProxy(BeanDefinitionHolder beanDefinitionHolder) {
        String beanName = beanDefinitionHolder.getBeanName();
        T proxy = (T)beanMap.get(beanName);
        if(proxy != null) {
            return proxy;
        }
        synchronized (object) {
            proxy = (T) beanMap.get(beanName);
            ProxyBulider proxyBulider = new ProxyBulider(beanDefinitionHolder);
            proxy = (T) proxyBulider.getJDKProxy(beanDefinitionHolder);
        }
        return proxy;
    }
//  根据Spring的思路，如果该类实现了接口，则优先获取jdk代理
    private <T> T doCreateProxy(BeanDefinitionHolder beanDefinitionHolder, Class<?> klass) {
        T proxy;
        if(klass.getInterfaces().length > 0) {
            proxy = cteateJDKProxy(beanDefinitionHolder);
        } else {
           proxy =  createCGLProxy(beanDefinitionHolder);
        }

        return proxy;
    }

    private void initBean(BeanDefinitionHolder beanDefinitionHolder) throws Exception {
        Class<?> klass = beanDefinitionHolder.getBeanClass();
        Object object = beanDefinitionHolder.getObject();
        Field[] fields = klass.getDeclaredFields();
        for(Field field : fields) {
            autowiredInjectionField(object, field);
        }
        autowiredMethod(klass, object);
    }
//  DI主要针对的是复杂类类型（用户自定义类类型），因此要求注入八大基本类型时注解必须要写value属性
// 否则将不进行处理，需要抛出异常
//  如果不是八大基本类型，则默认为是BeanFactory中的一个bean，即注入的是代理
    private Object autowiredInjectionField(Object object, Field field) throws Exception {
        if(field.isAnnotationPresent(Autowired.class)) {
            Autowired autowired = field.getAnnotation(Autowired.class);
            Class<?> fieldClass = field.getType();
            Object fieldObject;
            if(fieldClass.isPrimitive()) {
                String value = autowired.value();
                if(value.length() <= 0) {
                    throw new PrimitiveMustHaveValueAttributeException("八大基本类型的成员或参数" +
                            field + "必须含有value属性");
                }
//            这里将String类型的value转换为对应的八大基本类型并注入
                fieldObject = BeanUtils.stringToPrimitive(value, fieldClass);
            } else {
//                首先从BeanMap中获取，如果取不到，意味着循环依赖，其依赖的bean的代理还没有获取到，则处理循环依赖
                fieldObject = getBean(fieldClass.getName());
            }
            field.setAccessible(true);
            field.set(object, fieldObject);
        }
       return object;
    }

//  Method上加Bean注解的意思在该方法的返回值为BeanFactory中的一个Bean,在初始化类时执行
//  需要先注入成员，在执行方法，因为考虑的该方法中可能需要用到某些成员的值
//  方法执行完毕后要将其返回值加入到BeanMap中
//  自执行方法如果有参数，则每个参数都应该加上相应的注解，
//  否则无法对其参数进行赋值，应该抛出异常
//  并且参数的返回值类型不能为void,这里强行规定返回值类型也不能是八大基本类型
//  TODO 这里并不能处理多个方法返回值都相同的情况
    private Object autowiredMethod(Class<?> klass, Object object) throws BeanMethodReturnTypeIsErrorException {
        Method[] methods = klass.getDeclaredMethods();
        for (Method method : methods) {
            if(method.isAnnotationPresent(Bean.class)) {
                Bean bean = method.getAnnotation(Bean.class);
                String name = bean.name();
                Class<?> methodClass = method.getReturnType();
                if(methodClass.equals(void.class) || method.getReturnType().isPrimitive() ||
                        method.getParameters() != null) {
                    throw new BeanMethodReturnTypeIsErrorException("Method" + method + "返回值类型不能是"
                            + methodClass);
                }
//          TODO 这里先不处理有参数的方法,是否处理以后视情况而定
                String beanName = methodClass.getName();
                beanMap.put(beanName, doAutowiredMethod(object, method));
                if(name.length() > 0) {
                    aliasMap.put(name, beanName);
                }
            }
        }

        return object;
    }

    private Object doAutowiredMethod(Object object, Method method) {
        try {
            return method.invoke(object);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        return null;
    }

//    如果进行到这一步，获取的一定是代理
    @Override
    public <T> T createBean(BeanDefinitionHolder beanDefinitionHolder) {
        Class<?> beanClass = beanDefinitionHolder.getBeanClass();
        return doCreateProxy(beanDefinitionHolder, beanClass);
    }

    @Override
    public <T> T createCGLProxy(BeanDefinitionHolder beanDefinitionHolder) {
        return cglProxy(beanDefinitionHolder);
    }

    @Override
    public <T> T cteateJDKProxy(BeanDefinitionHolder beanDefinitionHolder) {
        return jdkProxy(beanDefinitionHolder);
    }

    @Override
    public void setBeanDefinitionHolder(BeanDefinitionHolder beanDefinitionHolder) {
        if (!beanDefinitionHolderMap.containsValue(beanDefinitionHolder)) {
            beanDefinitionHolderMap.put(beanDefinitionHolder.getBeanName(), beanDefinitionHolder);
        }
    }

    @Override
    public BeanDefinitionHolder getBeanDefinitionHolder(String beanName) {
        return beanDefinitionHolderMap.get(beanName);
    }

//    创建bean时需要考虑到循环依赖的情况，这里是通过对其成员进行注入解决的
//    TODO 以后还应该提供构造函数参数解析的方法
    @Override
    public <T> T getBean(String name) throws Exception {
        String beanName = name;
        BeanDefinitionHolder beanDefinitionHolder = beanDefinitionHolderMap.get(beanName);
        if(beanDefinitionHolder == null) {
            beanName = aliasMap.get(beanName);
            beanDefinitionHolder = beanDefinitionHolderMap.get(beanName);
        }
        if(beanDefinitionHolder == null) {
            throw new BeanDefinitionHolderIsNotExistException("未扫描到相关的Bean");
        }
        Object proxy = beanMap.get(beanName);
        if(proxy == null) {
            synchronized (createBeanObject) {
                proxy = beanMap.get(beanName);
                if (proxy == null) {
//                    通过injection来判断该Bean是否正在创建中，以此来解决循环依赖
                    if (!beanDefinitionHolder.isInjection()) {
                        proxy = createBean(beanDefinitionHolder);
                        beanDefinitionHolder.setProxy(proxy);
                        beanDefinitionHolder.changeInjection();
                        initBean(beanDefinitionHolder);
                        beanMap.put(beanName, proxy);
                        return (T) proxy;
                    } else {
                        return (T) beanDefinitionHolder.getObject();
                    }
                }
            }
            }
        return (T) proxy;
    }
}

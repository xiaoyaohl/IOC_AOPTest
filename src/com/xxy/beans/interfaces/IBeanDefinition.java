package com.xxy.beans.interfaces;

public interface IBeanDefinition {
//    虽然定义了SCOPE_PROTOTYPE，但是只准备实现单例模式
    String SCOPE_SINGLETON = "singleton";
    String SCOPE_PROTOTYPE = "prototype";

//    void setProxy(Object proxy);
//    Object getProxy();
//    返回原对象
    Object getObject();
    void steObject(Object object);
//      是否被注入
//      Spring通过BeanFactory中的类与类之间的依赖关系的Map,以及提前曝光单例来解决循环依赖
//      使用递归的方式来解决依赖注入
//      这里通过injection的返回值来确定单例是否被实例化
//      injection默认为false，实例化bean之后，需要将injection修改为true；

    void setBeanName(String beanName);
    String getBeanName();
}

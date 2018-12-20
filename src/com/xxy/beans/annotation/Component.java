package com.xxy.beans.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
//加载类上的注解，用以说明此类是beanFactory中的一个bean
public @interface Component {
//    name为bean的别名
    String name() default "";
}

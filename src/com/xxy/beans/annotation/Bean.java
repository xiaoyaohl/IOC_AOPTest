package com.xxy.beans.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.CONSTRUCTOR})
//将方法的返回值作为一个Bean存入BeanFactory中
//可以给name属性
//所有的name都可以被认为是别名,以完整的类名称做为beanName，不进行更复杂的修饰
public @interface Bean {
    String name() default "";
}

package com.xxy.beans.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.PARAMETER})
public @interface Autowired {
//    成员或参数可以是八大基本类型，也可以是bean
//    对于其他复杂类型这里先不做考虑
//    如果name不为空串，则认为该成员或参数就是一个bean
    String value() default "";
}

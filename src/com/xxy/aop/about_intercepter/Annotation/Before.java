package com.xxy.aop.about_intercepter.Annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
//前置拦截器注解
public @interface Before {
	Class<?> klass();
	String method();
	Class<?>[] parasType() default {};
}

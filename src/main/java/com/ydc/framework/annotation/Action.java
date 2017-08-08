package com.ydc.framework.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE) //作用在方法上
@Retention(RetentionPolicy.RUNTIME) //注解存在于运行时，通过反射读取
public @interface Action {
    String value();
}

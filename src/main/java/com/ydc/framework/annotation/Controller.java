package com.ydc.framework.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE) //只能作用在方法上
@Retention(RetentionPolicy.RUNTIME)//该注解在运行时存在，由java反射读取
public @interface Controller {
}

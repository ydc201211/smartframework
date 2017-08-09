package com.ydc.framework.helper;

import com.ydc.framework.annotation.Inject;
import com.ydc.framework.util.ArrayUtil;
import com.ydc.framework.util.ReflectionUtil;
import org.apache.commons.collections4.MapUtils;

import java.lang.reflect.Field;
import java.util.Map;

public class IocHelper {
    static{
        Map<Class<?>, Object> beanMap = BeanHelper.getBeanMap(); //框架需要管理的bean映射
        if(MapUtils.isNotEmpty(beanMap)){
            for(Map.Entry<Class<?>, Object> ent : beanMap.entrySet()){
                Class<?> beanCls = ent.getKey();
                Object beanInstance = ent.getValue();
                // 获取该class所有的成员属性,包括公共、保护、默认（包）访问和私有字段，但不包括继承的字段
                Field[] beanFields = beanCls.getDeclaredFields();
                if(ArrayUtil.isNotEmpty(beanFields)){
                    for (Field beanField : beanFields) {
                        //判断 该字段是否 包含 inject注解
                        if(beanField.isAnnotationPresent(Inject.class)){
                            Class<?> beanFieldType = beanField.getType(); //声明类型 成员变量的Class
                            Object beanFieldInstance = beanMap.get(beanFieldType); // 获取该类型的实例
                            if(beanFieldInstance != null){
                                ReflectionUtil.setField(beanInstance,beanField,beanFieldInstance);// 把对应的成员变量属性 赋值
                            }
                        }
                    }
                }
            }
        }
    }
}

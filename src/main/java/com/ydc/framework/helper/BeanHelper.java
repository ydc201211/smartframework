package com.ydc.framework.helper;


import com.ydc.framework.util.ReflectionUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public final class BeanHelper {
    /**
     * 1. 生成bean实例
     * 2. 获取bean示例
     */

    /** key : class value:对应的实例 也就是只能存在单例么?*/
    private static final Map<Class<?>,Object> BEAN_MAP = new HashMap<>();

    static {
        Set<Class<?>> beanClassSet = ClassHelper.getBeanClassSet();
        for (Class<?> c : beanClassSet) {
            BEAN_MAP.put(c,ReflectionUtil.newInstance(c));  //创建所有class的实例(自己定义的注解类)
        }
    }

    /** bean 容器**/
    public static Map<Class<?>, Object> getBeanMap() {
        return BEAN_MAP;
    }

    /** 获取bean**/
    public static <T> T getBean(Class<T> cls){
        if(!BEAN_MAP.containsKey(cls)){
            throw new RuntimeException("can not get bean by class : " + cls);
        }
        return (T) BEAN_MAP.get(cls);
    }

    /**  添加bean 实例**/
    public static void setBean(Class<?> cls,Object obj){
        BEAN_MAP.put(cls,obj);
    }
}

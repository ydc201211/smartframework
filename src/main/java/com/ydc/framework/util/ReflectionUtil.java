package com.ydc.framework.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public final class ReflectionUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(com.sun.deploy.util.ReflectionUtil.class);

    /** 创建实例 **/
    /**直接通过类创建实例*/
    public static Object newInstance(Class<?> cls){
        Object ins = null;
        try {
            ins = cls.newInstance();
        } catch (Exception e) {
            LOGGER.error("new instance failure");
            throw new RuntimeException(e);
        }
        return ins;
    }
    /**通过类名创建实例*/
    public static Object newInstance(String className){
        Object ins = null;
        try {
            Class<?> cls = Class.forName(className);
            ins = cls.newInstance();
        } catch (Exception e) {
            LOGGER.error("new instance failure");
            throw new RuntimeException(e);
        }
        return ins;
    }

    /**
     * 调用方法
     * @param obj 实例
     * @param method 方法
     * @param args 参数
     * @return
     */
    public static Object invokeMethod(Object obj,Method method,Object...args){
        method.setAccessible(true); //取消访问权限检查
        Object result = null;
        try {
            result = method.invoke(obj, args);
        } catch (Exception e) {
            LOGGER.error("invoe method fail",e);
            throw new RuntimeException(e);
        }
        return result;
    }

    /**
     * 调用不带参数的方法
     * @param obj
     * @param method
     * @return
     */
    public static Object invokeMethod(Object obj,Method method){
        method.setAccessible(true); //取消访问权限检查
        Object result = null;
        try {
            result = method.invoke(obj);
        } catch (Exception e) {
            LOGGER.error("invoe method fail",e);
            throw new RuntimeException(e);
        }
        return result;
    }

    /**
     * 设置成员变量
     * @param obj
     * @param field
     * @param value
     */
    public static void setField(Object obj,Field field, Object value){

        try {
            field.setAccessible(true); //取消访问权限检查
            field.set(obj,value);
        } catch (Exception e) {
            LOGGER.error("set Field fail",e);
            throw new RuntimeException(e);
        }
    }
}

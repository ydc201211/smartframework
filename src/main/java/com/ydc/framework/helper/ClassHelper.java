package com.ydc.framework.helper;

import com.ydc.framework.annotation.Controller;
import com.ydc.framework.annotation.Services;
import com.ydc.framework.util.ClassUtil;
import com.ydc.framework.util.PropsUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.Set;

public class ClassHelper {
    private static final Logger LOGGER = LoggerFactory.getLogger(PropsUtil.class);
    private static final Set<Class<?>> CLASS_SET;
    static{
        LOGGER.debug("是否被加载了两次");
        CLASS_SET = ClassUtil.getClassSet(ConfigHelper.getBasePackage());
        Set<Class<?>> classSet = ClassUtil.getClassSet("cn.mrcode.smartPluginSecurity");

    }

    public static Set<Class<?>> getClassSet() {
        return CLASS_SET;
    }

    /** 获取 所有services类型的注解类*/
    public static Set<Class<?>> getServiceClassSet(){
        Set<Class<?>> serviceClassSet = new HashSet<>();
        for (Class<?> c : CLASS_SET) {
            if(c.isAnnotationPresent(Services.class)){ // 是否存在 services注解
                serviceClassSet.add(c);
            };
        }
        return serviceClassSet;
    }

    /** 获取 所有Controller类型的注解类*/
    public static Set<Class<?>> getControllerClassSet(){
        Set<Class<?>> controllerClassSet = new HashSet<>();
        for (Class<?> c : CLASS_SET) {
            if(c.isAnnotationPresent(Controller.class)){
                controllerClassSet.add(c);
            };
        }
        return controllerClassSet;
    }

    /** 获取所有的 bean : controller 和 Services 注解的类**/
    public static Set<Class<?>> getBeanClassSet(){
        Set<Class<?>> beanClassSet = new HashSet<>();
        beanClassSet.addAll(getControllerClassSet());
        beanClassSet.addAll(getServiceClassSet());
        return beanClassSet;
    }

    /**
     * 获取 应用包名下某父类(或接口)的所有子类(或实现类)
     * @param superClass
     * @return
     */
    public static Set<Class<?>> getClassSetBySuper(Class<?>  superClass){
        Set<Class<?>> classSet = new HashSet<>();
        for (Class<?> cls : CLASS_SET) {
            //判定此 Class 对象所表示的类或接口与指定的 Class 参数所表示的类或接口是否相同，或是否是其超类或超接口。 并且不等于自己
            if(superClass.isAssignableFrom(cls) && !superClass.equals(cls)){
                classSet.add(cls);
            }
        }
        return classSet;
    }

    /**
     * 获取应用包名下 待有某注解的所有类
     * @param annotationClass
     * @return
     */
    public static Set<Class<?>> getClassSetByAnnotation(Class<? extends Annotation> annotationClass){
        Set<Class<?>> classSet = new HashSet<>();
        for (Class<?> cls : CLASS_SET) {
            if(cls.isAnnotationPresent(annotationClass)){
                classSet.add(cls);
            }
        }
        return classSet;
    }
}

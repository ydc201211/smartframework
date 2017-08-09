package com.ydc.framework.bean;

import java.lang.reflect.Method;

public class Handler {
    static{
        System.out.println("是否被加载了两次");
    }
    private Class<?> controllerClass; //controller类
    private Method actionMethod; //方法

    public Handler(Class<?> controllerClass, Method actionMethod) {
        this.controllerClass = controllerClass;
        this.actionMethod = actionMethod;
    }

    public Class<?> getControllerClass() {
        return controllerClass;
    }

    public Method getActionMethod() {
        return actionMethod;
    }
}

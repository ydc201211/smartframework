package com.ydc.framework.helper;

import com.ydc.framework.annotation.Action;
import com.ydc.framework.bean.Handler;
import com.ydc.framework.bean.Request;
import com.ydc.framework.util.ArrayUtil;
import org.apache.commons.collections4.CollectionUtils;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ControllerHelper {
    // 用于存放 请求 与 处理器的映射 关系
    private static final Map<Request,Handler> ACTION_MAP = new HashMap<>();
    static {
        Set<Class<?>> controllerClassSet = ClassHelper.getControllerClassSet();
        if(CollectionUtils.isNotEmpty(controllerClassSet)){
            for (Class<?> cls : controllerClassSet) {
                Method[] methods = cls.getDeclaredMethods(); // 获取所有的 方法
                for (Method method : methods) {
                    if(method.isAnnotationPresent(Action.class)){ //找到action注解的方法
                        Action action = method.getAnnotation(Action.class);
                        String mapping = action.value();
                        if(mapping.matches("\\w+:/\\w*")){
                            String[] array = mapping.split(":");
                            if(ArrayUtil.isNotEmpty(array) && array.length == 2){
                                //获取请求路径和请求方法
                                String requestMethod = array[0];
                                String requestPath = array[1];
                                Request request = new Request(requestMethod,requestPath);
                                Handler handler =  new Handler(cls,method);// hander保存的是, 对应的类  和 方法
                                ACTION_MAP.put(request,handler);  // 把 请求的方法 和路径封装成的request对象,和 处理 该路径的方法 所管理.到时候就能找得到执行谁了
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * 通过 请求类型  和  请求路径 获取 对应的处理类
     * @param requestMethod
     * @param requestPath
     * @return
     */
    public static  Handler getHander(String requestMethod,String  requestPath){
        return ACTION_MAP.get(new Request(requestMethod,requestPath));
    }
}

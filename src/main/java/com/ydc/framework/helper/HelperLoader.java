package com.ydc.framework.helper;

import com.ydc.framework.util.ClassUtil;

public class HelperLoader {
    public static void init(){
        Class<?>[] list ={
                ClassHelper.class,
                BeanHelper.class,
                IocHelper.class,
                ControllerHelper.class
        };

        for (Class<?> cls : list) {
            ClassUtil.loadClass(cls.getName(),true);
//            ClassUtil.loadClass(cls.getName(),false);
        }
    }
}

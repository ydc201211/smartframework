package com.ydc.framework.proxy;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;
import java.util.List;

/**
 *
 * 代理管理器,用来创建代理对象
 */
public class ProxyManger {
    /**
     * 创建代理
     * @param targetClass 目标类
     * @param proxyList 代理列表
     * @param <T>
     * @return
     */
    public static <T>T createProxy(final Class<?> targetClass,final List<Proxy> proxyList){
        return (T)Enhancer.create(targetClass, new MethodInterceptor() {
            @Override
            public Object intercept(Object targetObj, Method method, Object[] paramsObjects, MethodProxy methodProxy) throws Throwable {
                return new ProxyChain(targetClass,targetObj,method,methodProxy,paramsObjects,proxyList).doProxyChain();
            }
        });
    }
}

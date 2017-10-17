package com.ydc.framework.proxy;

public interface Proxy {

    /**
     * Created by zhuqiang on 2015/10/25 0025.
     * 代理接口
     */

    /** 执行链式代理 */
    Object doProxy(ProxyChain proxyChain) throws Throwable;

}

package com.ydc.framework.proxy;

import com.ydc.framework.annotation.Transaction;
import com.ydc.framework.helper.DataBaseHelper;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

/**
 * @author zhuqiang
 * @version V1.0
 * @date 2015/11/5 20:59
 * 事务界面代理类
 */
public class TransactionProxy implements Proxy {
    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(TransactionProxy.class);
    private static final ThreadLocal<Boolean> FLAG_HOLDER = new ThreadLocal<Boolean>(){  //用来存标志是否已经开启了事务
        @Override
        protected Boolean initialValue() {
            return false;
        }
    };
    @Override
    public Object doProxy(ProxyChain proxyChain) throws Throwable {
        Method method = proxyChain.getTargetMethod();
        Boolean flag = FLAG_HOLDER.get();
        Object result ;
        if(!flag && method.isAnnotationPresent(Transaction.class)){ //该方法还没有开启过事务，并且该方法有事务注解
            try {
                FLAG_HOLDER.set(true);
                DataBaseHelper.beginTransaction();  // 我们把jdbc的操作都封装在了 这个类里面。开启事务
                LOGGER.debug("begin transaction");
                result = proxyChain.doProxyChain();
                DataBaseHelper.commitTracsaction(); //提交事务
                LOGGER.debug("commit transaction");
            }catch (Exception e){
                DataBaseHelper.roolbackTransaction();  //r如果在执行目标（代理链条）方法的时候出现了错误，就回滚事务
                LOGGER.debug("roolback transaction");
                throw e;
            }finally {
                FLAG_HOLDER.remove();
            }
        }else{ // 如果没有事务注解，就直接执行代理链
            result = proxyChain.doProxyChain();
        }
        return result;
    }
}


package com.ydc.framework.helper;

import org.slf4j.LoggerFactory;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class ServletHelper {
    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(ServletHelper.class);

    //为每个线程存储自己的servletHelper
    private static final ThreadLocal<ServletHelper> SERVLET_HELPER_THREAD_LOCAL = new ThreadLocal<>();
    private HttpServletRequest request;
    private HttpServletResponse response;

    private ServletHelper(HttpServletRequest request, HttpServletResponse response) {
        this.request = request;
        this.response = response;
    }

    /** 初始化**/
    public static void init(HttpServletRequest request, HttpServletResponse response){
        SERVLET_HELPER_THREAD_LOCAL.set(new ServletHelper(request,response));
    }
    /** 销毁 */
    public static void destroy(){
        SERVLET_HELPER_THREAD_LOCAL.remove();
    }

    /** 获取rquest对象**/
    public static HttpServletRequest getRquest(){
        return SERVLET_HELPER_THREAD_LOCAL.get().request;
    }

    /** 获取response对象**/
    public static HttpServletResponse getResponse(){
        return SERVLET_HELPER_THREAD_LOCAL.get().response;
    }
    /** 获取session对象**/
    public static HttpSession getSession(){
        return getRquest().getSession();
    }

    public static ServletContext getServletContext(){
        return getRquest().getServletContext();
    }

    /** 将属性放入request中 */
    public static void setRequestAttribute(String name,Object value){
        getRquest().setAttribute(name,value);
    }
    /** 从request中获取值 */
    public static Object getRequestAttribute(String name){
        return getRquest().getAttribute(name);
    }
    /** 从request中移除 **/
    public static void removeRequestAttribute(String name){
        getRquest().removeAttribute(name);
    }

    public static void setSessionAttribute(String key,Object value){
        getSession().setAttribute(key,value);
    }
    public static Object getSessionAttribute(String name){
        return getSession().getAttribute(name);
    }
    public static void removeSessionAttribute(String name){
        getSession().removeAttribute(name);
    }

    public static void invalidateSession(){
        getSession().invalidate();
    }

    /** 重定向 **/
    public static void sendRedirect(String location){
        try {
            getResponse().sendRedirect(location);
        } catch (IOException e) {
            LOGGER.error("sendRedirect fail",e);
            throw  new RuntimeException(e);
        }
    }
}

package com.ydc.framework.helper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ydc.framework.bean.Data;
import com.ydc.framework.bean.Handler;
import com.ydc.framework.bean.Param;
import com.ydc.framework.bean.View;
import com.ydc.framework.util.ReflectionUtil;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.util.Map;

public class DispatcherServlet extends HttpServlet {
    @Override
    public void init(ServletConfig config) throws ServletException {
        HelperLoader.init();  // 就是加载之前封装好的 Helper等class
        ServletContext servletContext = config.getServletContext();

        // 查看了下 返回的是 org.apache.jasper.servlet.JspServlet 说明是框架中的一个servlet
        ServletRegistration jspServlet = servletContext.getServletRegistration("jsp"); //获取注册的 jsp servlet对象
        jspServlet.addMapping(ConfigHelper.getJspPath() + "*");

        // 注册servlet。 使用默认的就是放行。 这里就是让客户端请求能获取到资源文件
        // 符合该路径的，会被该servlet执行，而不会转调到我们自己的setvlet
        ServletRegistration aDefault = servletContext.getServletRegistration("default"); //处理静态资源
        aDefault.addMapping(ConfigHelper.getAssetPath() + "*", "/favicon.ico"); ///favicon.ico 是网站的标志图标也要放行

        // 然后这个是所有的servlet . 里面有我们自己的 和 框架注册的
        Map<String, ? extends ServletRegistration> servletRegistrations = servletContext.getServletRegistrations();
        UploadHelper.init(servletContext); //初始化文件请求数据类型
    }

    // 基础知识: 先进入service 在service方法中判断 然后调用不同的get啊post方法的
    @Override
    public void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            req.setCharacterEncoding("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        ServletHelper.init(req,resp);
        String reqMethod = req.getMethod().toLowerCase();
        String reqPath = req.getPathInfo(); // 获取请求路径
        Handler handler = ControllerHelper.getHander(reqMethod, reqPath); // 获取对应的处理类
        Param param = null;
        if (handler != null) { //找到了对应的处理器
            Class<?> controllerClass = handler.getControllerClass();
            Object bean = BeanHelper.getBean(controllerClass); // 处理器实例
            boolean isMultipart = UploadHelper.isMultipart(req);
            if (isMultipart) {
                param = UploadHelper.createParam(req);
            } else {
                param = RequestHelper.createParam(req);
            }
            Method actionMethod = handler.getActionMethod();
            Object result = null;
            if (param.isEmpty()) {
                result = ReflectionUtil.invokeMethod(bean, actionMethod);
            } else {
                result = ReflectionUtil.invokeMethod(bean, actionMethod,param);
            }
            handlerModelAndView(req, resp, result);
        }
        ServletHelper.destroy();

    }

    /**
     * 处理返回的结果。决定是跳转页面 还是 返回json数据
     *
     * @param req
     * @param resp
     * @param result
     *
     * @throws IOException
     * @throws ServletException
     */
    private void handlerModelAndView(HttpServletRequest req, HttpServletResponse resp, Object result) throws IOException, ServletException {
        // 处理 返回值, 需要区别对待 是 view 还是 data json数据
        if (result instanceof View) {
            View view = (View) result;
            String path = view.getPath();
            if (StringUtils.isNotBlank(path)) {
                if (path.startsWith("/")) {
                    resp.sendRedirect(req.getContextPath() + path); //
                } else { // 返回到页面的
                    Map<String, Object> model = view.getModel();
                    for (Map.Entry<String, Object> ent : model.entrySet()) {
                        req.setAttribute(ent.getKey(), ent.getValue());
                    }
                    req.getRequestDispatcher(ConfigHelper.getJspPath() + path).forward(req, resp);
                }
            }
        } else if (result instanceof Data) {
            Data data = (Data) result;
            Object model = data.getModel();
            if (model != null) {
                resp.setContentType("application/json");
                resp.setCharacterEncoding("UTF-8");
                PrintWriter writer = resp.getWriter();
                writer.write(new ObjectMapper().writeValueAsString(data));
                writer.flush();
                writer.close();
            }
        }
    }
}

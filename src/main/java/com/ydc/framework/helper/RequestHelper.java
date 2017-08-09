package com.ydc.framework.helper;

import com.ydc.framework.bean.FormParam;
import com.ydc.framework.bean.Param;
import com.ydc.framework.util.CodecUtil;
import com.ydc.framework.util.StreameUtil;
import com.ydc.framework.util.StringUtil;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

public final class RequestHelper {
    /**
     * 创建请求参数对象
     * @param request
     * @return
     */
    public static Param createParam(HttpServletRequest request) throws IOException {
        ArrayList<FormParam> formParamList = new ArrayList<>();
        formParamList.addAll(parseParameterNames(request));
        formParamList.addAll(parseInputStream(request));
        return new Param(formParamList);
    }

    /**
     * 解析参数
     * @param req
     * @return
     */
    private static List<FormParam> parseParameterNames(HttpServletRequest req){
        /**
         * 这样获取的方式是页面使用的 enctype="application/x-www-form-urlencoded" 以url encoded 提交的。
         * 或则是在url后面添加的get参数什么的正常方式提交的
         */
        List<FormParam> formParamList = new ArrayList<>();
        Enumeration<String> parameterNames = req.getParameterNames();
        while (parameterNames.hasMoreElements()) {
            String paramName = parameterNames.nextElement();
            String[] parameterValues = req.getParameterValues(paramName); // 如果页面传递的是相同name的参数。这里取到的就是一个数组
            if(ArrayUtils.isNotEmpty(parameterValues)){
                Object paramValue;
                if(parameterValues.length == 1){
                    paramValue = parameterValues[0];
                }else{
                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < parameterValues.length; i++) {
                        sb.append(parameterValues[i]);
                        if( i != parameterValues.length -1){
                            sb.append(StringUtil.SEPARATOR); //用逗号隔开
                        }
                    }
                    paramValue = sb.toString();
                }
                formParamList.add(new FormParam(paramName,paramValue));
            }
        }
        return formParamList;
    }

    /**
     * 从 流中解析参数。这里我还是没有明白 什么情况下会在流中出现这样类似的格式的字段，只知道xml的可以从流中解析出来
     * @return
     */
    private static List<FormParam> parseInputStream(HttpServletRequest req) throws IOException {
        List<FormParam> formParamList = new ArrayList<>();
        String body = CodecUtil.decodeURL(StreameUtil.getString(req.getInputStream()));
        if(StringUtils.isNotBlank(body)){
            String[] params = StringUtils.split(body, "&");
            for (String param : params) {
                String[] array = StringUtils.split(param, "=");
                if(ArrayUtils.isNotEmpty(array) && array.length ==2){
                    String paramName = array[0];
                    String paramValue = array[1];
                    formParamList.add(new FormParam(paramName,paramValue));
                }
            }
        }
        return formParamList;
    }
}

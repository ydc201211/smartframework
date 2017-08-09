package com.ydc.framework.bean;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class Request {
    // 请求方法
    private String requestMethod;
    //请求路径
    private String requestPath;

    public Request(String requestMethod, String requestPath) {
        this.requestMethod = requestMethod;
        this.requestPath = requestPath;
    }

    public String getRequestMethod() {
        return requestMethod;
    }

    public String getRequestPath() {
        return requestPath;
    }

    // 重写这两个方法 就是为了 在 map中以 request作为key的时候,避免我们自己手动的来重写 hashCode 和 equaks 方法
    // 因为我们认为.相同的 requestMethod 和 requestPath 就是匹配的. 需要转发到 对应的 hander中去

    @Override
    public int hashCode() {
        //通过反射字段获取code
        return HashCodeBuilder.reflectionHashCode(this);
    }

    @Override
    public boolean equals(Object obj) {
        //如果两个对象相等(是同一种class对象)当且仅当每个属性值都相等
        return EqualsBuilder.reflectionEquals(this,obj);
    }
}

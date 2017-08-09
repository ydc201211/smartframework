package com.ydc.framework.bean;

import java.util.HashMap;
import java.util.Map;

public class View {
    private String path;//路径名称
    private Map<String,Object> model;  //返回的数据模型

    public View(String path) {
        this.path = path;
        model = new HashMap<>();
    }

    public View addModel(String key,Object value){
        model.put(key,value);
        return this;
    }

    public String getPath() {
        return path;
    }

    public Map<String, Object> getModel() {
        return model;
    }

}

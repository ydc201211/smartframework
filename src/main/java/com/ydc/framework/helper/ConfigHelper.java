package com.ydc.framework.helper;

import com.ydc.framework.util.ConfigConstant;
import com.ydc.framework.util.PropsUtil;

import java.util.Properties;

public final class ConfigHelper {
    private static final Properties CONFIG_PROPS = PropsUtil.loadProps(ConfigConstant.CONFIG_FILE);
    public static String getJdbcDriver(){
        return PropsUtil.getString(CONFIG_PROPS,ConfigConstant.JDBC_DRIVER);
    }
    public static String getJdbcUrl(){
        return PropsUtil.getString(CONFIG_PROPS,ConfigConstant.JDBC_URL);
    }
    public static String getJdbcUsername(){
        return PropsUtil.getString(CONFIG_PROPS,ConfigConstant.JDBC_USERNAME);
    }
    public static String getJdbcPassword(){
        return PropsUtil.getString(CONFIG_PROPS,ConfigConstant.JDBC_PASSWORD);
    }
    public static String getBasePackage(){
        return PropsUtil.getString(CONFIG_PROPS,ConfigConstant.APP_BASE_PACKAGE);
    }
    /** 该属性为可选*/
    public static String getJspPath(){
        return PropsUtil.getString(CONFIG_PROPS,ConfigConstant.APP_JSP_PATH,"/WEB-INF/view/");
    }
    /** 该属性为可选*/
    public static String getAssetPath(){
        return PropsUtil.getString(CONFIG_PROPS,ConfigConstant.APP_ASSET_PATH,"/asset/");
    }
    /** 该属性为可选*/
    public static int getUploadLimit() {
        return PropsUtil.getInt(CONFIG_PROPS,ConfigConstant.UPLOAD_LIMIT,0);
    }

    public static String getString(String key) {
        return PropsUtil.getString(CONFIG_PROPS,key);
    }

    public static boolean getBoolean(String key) {
        return PropsUtil.getBoolean(CONFIG_PROPS,key);
    }
}

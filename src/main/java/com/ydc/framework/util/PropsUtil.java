package com.ydc.framework.util;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropsUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(PropsUtil.class);

    /**
     * 加载属性文件
     *
     * @param fileName
     *
     * @return
     */
    public static Properties loadProps(String fileName) {
        Properties props = null;
        InputStream is = null;
        try {
            is = Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName);
            if (is == null) {
                throw new FileNotFoundException(fileName + " 文件未找到");
            }
            props = new Properties();
            props.load(is);
        } catch (IOException e) {
            LOGGER.error("load properties file failure", e);
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    LOGGER.error("close input stream failure", e);
                }
            }
        }
        return props;
    }

    /**
     * 获取字符型 属性值
     *
     * @param props
     * @param key
     *
     * @return
     */
    public static String getString(Properties props, String key) {
        return getString(props, key, "");
    }

    /**
     * 获取字符型 属性值
     *
     * @param props
     * @param key
     * @param defautValue 未获取到,返回的默认值
     *
     * @return
     */
    public static String getString(Properties props, String key, String defautValue) {
        String value = defautValue;
        if (props.containsKey(key)) {
            value = props.getProperty(key);
        }
        return value;
    }

    public static int getInt(Properties props, String key) {
        return getInt(props, key, 0);
    }

    public static int getInt(Properties props, String key, int defautValue) {
        int value = defautValue;
        if (props.containsKey(key)) {
            value = CastUtil.castInt(props.getProperty(key));
        }
        return value;
    }

    public static long getLong(Properties props, String key) {
        return getLong(props, key, 0);
    }

    public static long getLong(Properties props, String key, long defautValue) {
        long value = defautValue;
        if (props.containsKey(key)) {
            value = CastUtil.castInt(props.getProperty(key));
        }
        return value;
    }

    public static double getDouble(Properties props, String key) {
        return getDouble(props, key, 0);
    }

    public static double getDouble(Properties props, String key, double defautValue) {
        double value = defautValue;
        if (props.containsKey(key)) {
            value = CastUtil.castDouble(props.getProperty(key));
        }
        return value;
    }

    public static boolean getBoolean(Properties props,String key){
        return getBoolean(props,key,false);
    }
    public static boolean getBoolean(Properties props,String key,boolean defautValue) {
        boolean value = defautValue;
        if (props.containsKey(key)) {
            value = CastUtil.castBoolean(props.getProperty(key));
        }
        return value;
    }
}

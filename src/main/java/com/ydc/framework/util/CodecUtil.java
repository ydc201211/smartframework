package com.ydc.framework.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

public class CodecUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(CodecUtil.class);
    public static String encodeURL(String source){
        String target;
        try {
            target = URLEncoder.encode(source, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            LOGGER.error("encode url fail",e);
            throw new RuntimeException(e);
        }
        return target;
    }

    public static String decodeURL(String source){
        String target;
        try {
            target = URLDecoder.decode(source, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            LOGGER.error("decode url fail",e);
            throw new RuntimeException(e);
        }
        return target;
    }
}

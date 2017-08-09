package com.ydc.framework.util;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

public class FileUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(FileUtil.class);
    /**
     * 获取真实文件名称，自动去掉路径，防止中文乱码
     *
     * @param fileName 从页面file组件上传过来的路径加文件名称
     *
     * @return
     */
    public static String getRealFileName(String fileName) {
        return FilenameUtils.getName(fileName); // apache.io 的工具类
    }

    /**
     * 创建文件夹
     *
     * @param filePath 文件真实路径
     *
     * @return
     */
    public static File createFile(String filePath) {
        File file = null;
        try {
            file = new File(filePath);
            File parentDir = file.getParentFile(); //获取父目录
            if (!parentDir.exists()) {

                FileUtils.forceMkdir(parentDir);

            }
        } catch (IOException e) {
            LOGGER.error("create file failure",e);
            throw new RuntimeException(e);
        }
        return file;
    }
}
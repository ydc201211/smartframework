package com.ydc.framework.helper;

import com.ydc.framework.bean.FileParam;
import com.ydc.framework.bean.FormParam;
import com.ydc.framework.bean.Param;
import com.ydc.framework.util.FileUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class UploadHelper {
    private static final Logger LOGGER = LoggerFactory.getLogger(UploadHelper.class);
    private static ServletFileUpload servletFileUpload; // apache 的文件上传对象

    /**
     * 初始化，文件上传对象
     * @param servletContext
     */
    public static void init(ServletContext servletContext){
        File repository = (File)servletContext.getAttribute("javax.servlet.context.tempdir"); // 获取应用服务器的临时目录
        // 设置临时文件目录的 临界值大小，和临时文件夹目录
        servletFileUpload = new ServletFileUpload(new DiskFileItemFactory(DiskFileItemFactory.DEFAULT_SIZE_THRESHOLD,repository));
        int uploadLimit = ConfigHelper.getUploadLimit(); //获取 用户限制上传文件的大小
        if(uploadLimit != 0){
            servletFileUpload.setFileSizeMax(uploadLimit);
        }
    }

    /**
     * 判断该请求是否是 Multipart类型
     * @param req
     * @return
     */
    public static boolean isMultipart(HttpServletRequest req){
        return ServletFileUpload.isMultipartContent(req);
    }

    /**
     * 创建请求参数
     * @param req
     * @return
     */
    public static Param createParam(HttpServletRequest req) throws IOException {
        List<FormParam> formParamList = new ArrayList<>(); //表单参数列表
        List<FileParam> fileParamList = new ArrayList<>(); //文件参数列表
        try {
            Map<String, List<FileItem>> fileItemListMap = servletFileUpload.parseParameterMap(req);
            if(MapUtils.isNotEmpty(fileItemListMap)) {
                for(Map.Entry<String, List<FileItem>> ent : fileItemListMap.entrySet()){
                    String filedName = ent.getKey();
                    List<FileItem> value = ent.getValue();
                    if(CollectionUtils.isNotEmpty(value)){
                        for (FileItem fileItem : value) {
                            if(fileItem.isFormField()){ //如果是表单属性
                                String fieldValue = fileItem.getString("utf-8");
                                formParamList.add(new FormParam(filedName, fieldValue));
                            }else{ //如果是文件字段
                                String fileName = FileUtil.getRealFileName(new String(fileItem.getName().getBytes(),"UTF-8")); //获取真实文件名称，自动去掉路径，防止中文乱码
                                if(StringUtils.isNotBlank(fileName)){
                                    String fieldName = fileItem.getFieldName(); //字段名称
                                    long size = fileItem.getSize(); //文件大小
                                    String contentType = fileItem.getContentType(); //文件类型
                                    InputStream inputStream = fileItem.getInputStream(); //文件流
                                    fileParamList.add(new FileParam(fieldName, fileName, contentType, size, inputStream));
                                }
                            }
                        }
                    }
                }
            }
        } catch (FileUploadException e) {
            LOGGER.error("create Param failure",e);
            throw new RuntimeException(e);
        }
        return  new Param(formParamList,fileParamList);
    }

    /**
     * 上传文件
     * @param basePath 基础绝对路径
     * @param fileParam
     */
    public static void uploadFile(String basePath,FileParam fileParam){
        try {
            if(fileParam != null){
                String filePath = basePath + fileParam.getFileName();
                FileUtil.createFile(filePath);
                //使用 apache io工具进行图片的上传
                BufferedOutputStream output = new BufferedOutputStream(new FileOutputStream(filePath));
                IOUtils.copy(new BufferedInputStream(fileParam.getInputStream()), output);
                output.flush();
                output.close();
            }
        }catch (Exception e){
            LOGGER.error("upload file failure",e);
            throw new RuntimeException(e);
        }
    }

    /**
     * 批量上传文件
     * @param basePath
     * @param fileParamList
     */
    public static void uploadFile(String basePath,List<FileParam> fileParamList){
        try {
            if(CollectionUtils.isNotEmpty(fileParamList)){
                for (FileParam fileParam : fileParamList) {
                    if(fileParam != null){
                        String filePath = basePath + fileParam.getFileName();
                        FileUtil.createFile(filePath);
                        //使用 apache io工具进行图片的上传
                        IOUtils.copy(new BufferedInputStream(fileParam.getInputStream()),new BufferedOutputStream(new FileOutputStream(filePath)));
                    }
                }
            }
        }catch (Exception e){
            LOGGER.error("upload file failure",e);
            throw new RuntimeException(e);
        }
    }
}

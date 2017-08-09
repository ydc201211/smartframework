package com.ydc.framework.bean;

import com.ydc.framework.util.CastUtil;
import com.ydc.framework.util.PropsUtil;
import com.ydc.framework.util.StringUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.map.HashedMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Param {
    private static final Logger LOGGER = LoggerFactory.getLogger(PropsUtil.class);
    private List<FormParam> formParamList; //表单参数列表
    private List<FileParam> fileParamList; //文件参数列表

    public Param(List<FormParam> formParamList) {
        this.formParamList = formParamList;
    }

    public Param(List<FormParam> formParamList, List<FileParam> fileParamList) {
        this.formParamList = formParamList;
        this.fileParamList = fileParamList;
    }

    /**
     * 获取 表单参数映射
     * @return
     */
    public Map<String,Object> getFieldMap(){
        Map<String,Object> fieldMap = new HashedMap<>();
        if(CollectionUtils.isNotEmpty(formParamList)){
            for (FormParam formParam : formParamList) {
                String fieldName = formParam.getFieldName();
                Object value = formParam.getValue();
                if(fieldMap.containsKey(fieldName)){ //由于表单参数的处理有两种，parseParameterNames parseInputStream，正常的取和从流中取，在这里需要合并下相同的结果
                    value = fieldMap.get(fieldName) + StringUtil.SEPARATOR + value; //使用风隔符（自定义的逗号）分割开
                }
                fieldMap.put(fieldName,value);
            }
        }
        return fieldMap;
    }

    /**
     * 获取文件参数映射
     * @return
     */
    public Map<String,List<FileParam>> getFileMap(){
        Map<String,List<FileParam>> fileParamMap = new HashedMap<>();
        if(CollectionUtils.isNotEmpty(fileParamList)){
            for (FileParam fileParam : fileParamList) {
                String fieldName = fileParam.getFieldName();
                List<FileParam> fileList;
                if(fileParamMap.containsKey(fieldName)){ //处理多文件数组列表
                    fileList = fileParamMap.get(fieldName);
                }else{
                    fileList = new ArrayList<>();
                }
                fileList.add(fileParam);
                fileParamMap.put(fieldName,fileList);
            }
        }
        return fileParamMap;
    }

    /**
     * 按参数名称获取文件列表（多文件上传）
     * @param filedName
     * @return
     */
    public List<FileParam> getFileList(String filedName){
        return this.getFileMap().get(filedName);
    }

    /**
     * 按参数名称获取 唯一的 文件对象（单文件上传）
     * @param filedName
     * @return
     */
    public FileParam getFile(String filedName){
        List<FileParam> fileList = this.getFileList(filedName);
        if(CollectionUtils.isNotEmpty(fileList) && fileList.size() == 1){
            return fileList.get(0);
        }
        LOGGER.info(String.format("filedName:%s is not find",filedName));
        return null;
    }

    /** 验证参数是否为空 **/
    public boolean isEmpty(){
        return CollectionUtils.isEmpty(formParamList) && CollectionUtils.isEmpty(fileParamList);
    }

    /**
     * 根据参数名 获取 string类型参数值
     * @param name
     * @return
     */
    public String getString(String name){
        return CastUtil.castString(getFieldMap().get(name));
    }
    public double getDouble(String name){
        return CastUtil.castDouble(getFieldMap().get(name));
    }
    public long getLong(String name){
        return CastUtil.castLong(getFieldMap().get(name));
    }
    public int getInt(String name){
        return CastUtil.castInt(getFieldMap().get(name));
    }
    public boolean getBoolean(String name){
        return CastUtil.castBoolean(getFieldMap().get(name));
    }
}

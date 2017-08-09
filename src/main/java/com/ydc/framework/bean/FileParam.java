package com.ydc.framework.bean;

import java.io.InputStream;

public class FileParam {
    String fieldName; //参数名称
    String fileName; //文件名称
    String contentType; //文件类型
    long fileSize; //文件大小
    InputStream inputStream; //流

    public FileParam(String fieldName, String fileName, String contentType, long fileSize, InputStream inputStream) {
        this.fieldName = fieldName;
        this.fileName = fileName;
        this.contentType = contentType;
        this.fileSize = fileSize;
        this.inputStream = inputStream;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    public void setInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }
}

package com.opensource.msgui.commons.utils.model;

import java.io.InputStream;

public class EmailFileVo {

    private String fileName;//文件名
    private InputStream inputStream;//文件流

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    public void setInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }
}

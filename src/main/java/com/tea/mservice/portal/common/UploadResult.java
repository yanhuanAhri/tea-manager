package com.tea.mservice.portal.common;

public class UploadResult {
    private String file;
    private String md5;

    public UploadResult() {
    }

    public UploadResult(String file, String md5) {
        this.file = file;
        this.md5 = md5;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }
}

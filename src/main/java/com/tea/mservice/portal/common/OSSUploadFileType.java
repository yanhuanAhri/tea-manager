package com.tea.mservice.portal.common;

public enum  OSSUploadFileType {
    /**
     * 广告
     */
    AD("ad"),
    /**
     * app软件包
     */
    APK("apk"),
    /**
     * 合同
     */
    CONTRACT("contract"),
    /**
     * 商户信息
     */
    SHOP_INFO("shop-info"),
    /**
     * 二维码
     */
    QRCODE("qrcode")
    ;

    private String value;
    OSSUploadFileType(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }


}

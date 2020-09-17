package com.faster.enums;

/**
 * 爬虫相关枚举
 *
 * @author da.yang@hand-china.com
 * @date 2020/8/19
 */
public enum ReptileEnum {
    MP3("mp3", "音乐"),
    IMG("img", "图片"),
    TXT("txt","小说");

    private String code;
    private String msg;

    ReptileEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public static String getCode(String code) {

        return null;
    }

}

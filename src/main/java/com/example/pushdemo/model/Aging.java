package com.example.pushdemo.model;

/**
 * @author 林杰炜 Linjw
 * @Title TODO 类描述
 * @date 2020/5/22 10:18
 */
public enum Aging {

    GENERAL((short) 1, "普通"),
    URGENT((short) 2, "紧急"),
    IMPORTANT((short) 3, "重要");

    private short code;
    private String desc;

    private Aging(short code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public short getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}

package com.example.pushdemo.vivo;

import lombok.Data;

/**
 * @author linjw
 * @Title VIVO Push 回调 TaskVO
 * @date 2020/1/2 18:04
 */
@Data
public class VivoTaskVO {
    /**
     * 自定义参数值
     */
    private String param;
    /**
     * regId列表,用逗号隔开
     */
    private String targets;
}

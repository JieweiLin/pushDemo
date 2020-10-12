package com.example.pushdemo.model;

import lombok.Data;

import java.sql.Timestamp;

/**
 * @author 林杰炜 Linjw
 * @Title 第三方推送平台配置信息
 * @date 2020/5/21 18:13
 */
@Data
public class PushConfig {

    private Long id;
    private String appId;
    private Short appType;
    private String serviceAppId;
    private String serviceAppKey;
    private String serviceAppSecret;
    private String serviceMasterSecret;
    private Short pushChannel;
    private Timestamp createTime;
    private Timestamp updateTime;

}

package com.example.pushdemo.model;

import lombok.Data;

import java.sql.Timestamp;

/**
 * @author 林杰炜 Linjw
 * @Title 第三方推送Token与内部设备关系
 * @date 2020/5/21 17:24
 */
@Data
public class PushInfo {
    private Long id;
    private String deviceId;
    private String appId;
    private Short pushChannel;
    private String deviceToken;
    private Timestamp createTime;
    private Timestamp updateTime;
}

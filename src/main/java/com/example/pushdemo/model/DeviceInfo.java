package com.example.pushdemo.model;

import lombok.Data;

import java.sql.Timestamp;

/**
 * @author 林杰炜 Linjw
 * @Title 设备信息
 * @date 2020/5/21 18:11
 */
@Data
public class DeviceInfo {

    private Long id;
    private String deviceId;
    private String appId;
    private String appVersion;
    private String deviceModel;
    private String systemType;
    private String systemVersion;
    private String pushChannels;
    private Timestamp createTime;
    private Timestamp updateTime;
}

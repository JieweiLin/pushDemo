package com.example.pushdemo.model;

import lombok.Data;

import java.sql.Timestamp;

/**
 * @author 林杰炜 Linjw
 * @Title 内部应用
 * @date 2020/5/21 17:26
 */
@Data
public class AppInfo {
    private Long id;
    private String appId;
    private String appName;
    private String packageName;
    private Short appType;
    private Timestamp createTime;
    private Timestamp updateTime;
}

package com.example.pushdemo.model;

import lombok.Data;

import java.sql.Timestamp;

/**
 * @author 林杰炜 Linjw
 * @Title 推送规则信息
 * @date 2020/5/21 18:20
 */
@Data
public class PushRule {
    private Long id;
    private String pushCode;
    private Integer numTimes;
    private Short unit;
    private String description;
    private Timestamp createTime;
    private Timestamp updateTime;
}

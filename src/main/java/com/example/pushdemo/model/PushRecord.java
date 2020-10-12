package com.example.pushdemo.model;

import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * @author 林杰炜 Linjw
 * @Title 推送记录
 * @date 2020/5/22 10:27
 */
@Data
public class PushRecord implements Serializable {

    private String messageQueueId;
    private String msgId;
    private String appId;
    private String deviceId;
    private String deviceToken;
    private Short pushChannel;
    private String alertMsg;
    private String transmissionContent;
    private String tag;
    private Timestamp sendTime;
    private Timestamp receiveTime;
    private Timestamp pushTime;
    private String pushChannelMessageId;
    private Short state = 0;
    private String reason;
    private Integer costTime;
    private String clientInfo;
    private Timestamp createTime;
}

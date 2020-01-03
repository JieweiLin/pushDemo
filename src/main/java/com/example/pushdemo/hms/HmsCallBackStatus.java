package com.example.pushdemo.hms;

import lombok.Data;

/**
 * @author linjw
 * @Title 华为回调内容
 * @date 2020/1/2 17:55
 */
@Data
public class HmsCallBackStatus {
    /**
     * Push时ext.biTag值
     */
    private String biTag;
    /**
     * HUAWEI Push 应用Id
     */
    private String appid;
    /**
     * PushToken
     */
    private String token;
    /**
     * Push消息状态
     * 参照华为消息回执状态: https://developer.huawei.com/consumer/cn/doc/development/HMS-Guides/push-receipt
     */
    private Integer status;
    /**
     * 消息到达终端的时间戳（本地时间）
     */
    private String timestamp;
    /**
     * 请求ID，在发送接口中返回的值
     */
    private String requestId;
}

package com.example.pushdemo.model;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

/**
 * @author 林杰炜 Linjw
 * @Title 单条推送对象
 * @date 2020/5/22 10:08
 */
@Data
public class SingleMessage implements Serializable {

    /**
     * 设备信息
     */
    private DeviceInfo deviceInfo;
    /**
     * 推送模式
     */
    private Short pushMode = 1;
    /**
     * 发送时间
     */
    private Date sendTime = new Date();
    /**
     * 消息Id
     */
    private String msgId = UUID.randomUUID().toString().trim().replace("-", "").toUpperCase();
    /**
     * 弹窗标题
     */
    private String title;
    /**
     * 弹窗内容
     */
    private String alertMsg;
    /**
     * 透传内容
     */
    private Object transmissionContent;
    /**
     * 标签（推送编码）
     */
    private String tag;
    /**
     * 推送编码
     */
    @NotNull
    private String pushCode;
    /**
     * 离线推送时效{@link Aging}
     */
    private Aging aging;

}

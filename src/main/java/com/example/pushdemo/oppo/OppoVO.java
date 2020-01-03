package com.example.pushdemo.oppo;

import lombok.Data;

/**
 * @author linjw
 * @Title OPPO回执对象
 * @date 2020/1/3 11:41
 */
@Data
public class OppoVO {
    /**
     * Push 消息状态
     */
    private String eventType;
    /**
     * 消息Id
     */
    private String messageId;
    /**
     * 参数
     */
    private String param;
    /**
     *regId列表
     */
    private String registrationIds;
    /**
     * 任务Id
     */
    private String taskId;
}

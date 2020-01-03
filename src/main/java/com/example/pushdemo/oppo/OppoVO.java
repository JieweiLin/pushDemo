package com.example.pushdemo.oppo;

import com.alibaba.fastjson.annotation.JSONField;
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
    @JSONField(name = "eventType")
    private String eventType;
    /**
     * 消息Id
     */
    @JSONField(name = "messageId")
    private String messageId;
    /**
     * 参数
     */
    @JSONField(name = "param")
    private String param;
    /**
     *regId列表
     */
    @JSONField(name = "registrationIds")
    private String registrationIds;
    /**
     * 任务Id
     */
    @JSONField(name = "taskId")
    private String taskId;
}

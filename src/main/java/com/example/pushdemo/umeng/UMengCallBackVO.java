package com.example.pushdemo.umeng;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

/**
 * @author linjw
 * @Title 友盟回执对象
 * @date 2020/1/3 13:36
 */
@Data
public class UMengCallBackVO {
    /**
     * 发送消息时填写的thirdparth_id参数
     */
    @JSONField(name = "thirdparty_id")
    private String thirdPartyId;
    /**
     * 回执类型 0-送达 1-打开
     */
    @JSONField(name = "action_type")
    private Byte actionType;
    /**
     * 发送消息时生成的消息唯一标识
     */
    @JSONField(name = "msg_id")
    private String msgId;
    /**
     * 送达/打开消息的设备列表
     */
    @JSONField(name = "device_tokens")
    private String deviceTokens;
    /**
     * 送达/打开消息的alias列表
     */
    @JSONField(name = "alias")
    private String alias;
    /**
     * 送达通道, 如accs、xiaomi、huawei等
     */
    @JSONField(name = "channel")
    private String channel;
}

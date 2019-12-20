package com.example.pushdemo.xiaomi;

import lombok.Data;

import java.sql.Timestamp;
import java.util.Map;

/**
 * @author linjw
 * @Title 小米回调数据
 * @date 2019/12/20 17:04
 */
@Data
public class MiVO {
    /**
     * 开发者上传的自定义参数值
     */
    private String param;
    /**
     * 消息状态类型:
     * 1-送达;
     * 2-点击;
     * 16-无法找到目标设备;
     * 32-客户端调用disablePush接口禁用Push;
     * 64-目标设备不符合过滤条件
     */
    private Integer type;
    /**
     * 一批alias、regId或useraccount列表, 逗号分隔
     */
    private String targets;
    /**
     * 发送消息时设置的jobkey值
     */
    private String jobkey;
    /**
     * 消息送达时通知栏的状态
     * Enable-用户允许此app展示通知栏消息
     * Disable-通知栏消息已关闭
     * Unknown-通知栏状态未知
     */
    private String barStatus;
    /**
     * 消息送达设备的时间
     */
    private Timestamp timestamp;
    /**
     * type=16时返回的无效目标的子类
     * 1-无效regId
     * 2-无效alias
     * 3-无效useraccount
     */
    private Integer errorCode;
    /**
     * 该发送目标无法正常送达,但建议可替换成另一个新的目标标识以满足该设备的正常推送
     * key-该设备原始发送目标标识,value-建议替换成的新的目标标识
     */
    private Map<String, String> replaceTarget;
    /**
     * 该发送目标的区域有误
     * key-区域有误的发送目标标识,value-设备建议推送的区域(CN/GLOBAL,国内/国外)
     */
    private Map<String, String> targetRegion;
}

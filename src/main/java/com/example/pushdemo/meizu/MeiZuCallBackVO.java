package com.example.pushdemo.meizu;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.util.Map;

/**
 * @author linjw
 * @Title 魅族 callback
 * @date 2020/1/3 15:10
 */
@Data
public class MeiZuCallBackVO {
    /**
     * 回执接口访问令牌（推送平台设置回执地址令牌）
     */
    @JSONField(name = "access_token")
    private String accessToken;
    /**
     * 回执明细内容（json数据）
     * 如: {"msgId-1":{"param":"param","type":1,"targets":["pushRegId"]}}
     */
    @JSONField(name = "cb")
    private Map<String, MeiZuCbVO> cb;
}

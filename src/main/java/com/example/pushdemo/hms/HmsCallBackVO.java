package com.example.pushdemo.hms;

import lombok.Data;

import java.util.List;

/**
 * @author linjw
 * @Title 华为回调VO
 * @date 2020/1/2 18:01
 */
@Data
public class HmsCallBackVO {
    /**
     * 回执消息内容
     */
    private List<HmsCallBackStatus> statuses;
}

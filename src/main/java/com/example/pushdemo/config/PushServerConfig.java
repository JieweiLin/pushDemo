package com.example.pushdemo.config;

import lombok.Data;
import org.springframework.stereotype.Component;

/**
 * @author 林杰炜 Linjw
 * @Title TODO 类描述
 * @date 2020/9/27 10:36
 */
@Data
@Component
public class PushServerConfig {

    /**
     * 不允许发送时间段(开始时间)
     */
    private String denyStartTime;

    /**
     * 不允许发送时间段(结束时间)
     */
    private String denyEndTime;

    /**
     * 消息延迟超过时间不推送, 单位分钟
     */
    private String delayTime;
}

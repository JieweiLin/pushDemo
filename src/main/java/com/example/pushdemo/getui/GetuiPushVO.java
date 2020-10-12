package com.example.pushdemo.getui;

import lombok.Data;

/**
 * @author 林杰炜 Linjw
 * @Title TODO 类描述
 * @date 2020/5/21 16:06
 */
@Data
public class GetuiPushVO {

    private String title;

    private String content;

    private String transmissionContent;

    private Boolean offLine;

    private Long offLineExpireTime;

    private String clientId;
}

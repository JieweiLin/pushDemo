package com.example.pushdemo.common;

import lombok.Getter;

/**
 * @author 林杰炜 Linjw
 * @Title 推送渠道枚举
 * @date 2020/9/29 11:21
 */
@Getter
public enum PushChannelEnum {

    GE_TUI((short) 1, "个推"),
    U_MENG((short) 2, "友盟"),
    MI_PUSH((short) 3, "小米"),
    GE_TUI_APNS((short) 4, "个推APNs"),
    HMS_PUSH((short) 5, "华为"),
    MEI_ZU_PUSH((short) 6, "魅族"),
    OPPO_PUSH((short) 7, "OPPO"),
    VIVO_PUSH((short) 8, "VIVO"),
    JPUSH((short) 9, "极光"),
    U_MENG_APNS((short) 10, "友盟APNs");

    private short key;

    private String value;

    PushChannelEnum(short key, String value) {
        this.key = key;
        this.value = value;
    }
}

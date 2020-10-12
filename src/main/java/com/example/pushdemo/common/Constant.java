package com.example.pushdemo.common;

/**
 * @author 林杰炜 Linjw
 * @Title TODO 类描述
 * @date 2020/9/27 11:30
 */
public class Constant {

    public static final Short APP_TYPE_ANDROID = 1;
    public static final Short APP_TYPE_IOS = 2;

    public static final short PUSH_CHANNEL_GETUI = 1;
    public static final short PUSH_CHANNEL_UMENG = 2;
    public static final short PUSH_CHANNEL_XIAOMI = 3;
    public static final short PUSH_CHANNEL_GETUI_APNS = 4;
    public static final short PUSH_CHANNEL_HMS = 5;
    public static final short PUSH_CHANNEL_MEIZU = 6;
    public static final short PUSH_CHANNEL_OPPO = 7;
    public static final short PUSH_CHANNEL_VIVO = 8;
    public static final short PUSH_CHANNEL_JPUSH = 9;
    public static final short PUSH_CHANNEL_UMENG_APNS = 10;

    public static PushChannelEnum getPushChannelEnum(short key) {
        for (PushChannelEnum channelEnum : PushChannelEnum.values()) {
            if (channelEnum.getKey() == key) {
                return channelEnum;
            }
        }
        return null;
    }
}

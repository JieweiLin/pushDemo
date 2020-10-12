package com.example.pushdemo.handler;

import com.example.pushdemo.cache.CacheHandler;
import com.example.pushdemo.common.Constant;
import com.example.pushdemo.model.AppInfo;
import com.example.pushdemo.model.DeviceInfo;
import com.example.pushdemo.model.PushConfig;
import com.example.pushdemo.model.PushInfo;
import com.example.pushdemo.model.PushRecord;
import com.example.pushdemo.model.PushResult;
import com.example.pushdemo.model.SingleMessage;

import java.util.Objects;

/**
 * @author 林杰炜 Linjw
 * @Title TODO 类描述
 * @date 2020/10/12 14:45
 */
public class UMengAPNSPushHandler extends BasePushHandler {
    private volatile static UMengAPNSPushHandler uMengAPNSPushHandler;
    private final static short UMENG_APNS_PUSH_CHANNEL = Constant.PUSH_CHANNEL_UMENG_APNS;

    private UMengAPNSPushHandler() {
    }

    public static UMengAPNSPushHandler getInstance() {
        if (Objects.isNull(uMengAPNSPushHandler)) {
            synchronized (UMengAPNSPushHandler.class) {
                if (Objects.isNull(uMengAPNSPushHandler)) {
                    uMengAPNSPushHandler = new UMengAPNSPushHandler();
                }
            }
        }
        return uMengAPNSPushHandler;
    }

    @Override
    PushResult handler(String no, SingleMessage singleMessage, PushRecord pushRecord, int passThrough, AppInfo appInfo, PushConfig pushConfig, String pushToken) {

        return null;
    }

    @Override
    PushInfo getPushInfo(DeviceInfo deviceInfo) {
        return CacheHandler.getPushInfo(deviceInfo, UMENG_APNS_PUSH_CHANNEL);
    }
}

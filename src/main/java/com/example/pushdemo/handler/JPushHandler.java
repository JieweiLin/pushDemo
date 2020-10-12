package com.example.pushdemo.handler;

import cn.jiguang.common.ClientConfig;
import cn.jpush.api.JPushClient;
import cn.jpush.api.push.model.Message;
import cn.jpush.api.push.model.Options;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;
import com.example.pushdemo.cache.CacheHandler;
import com.example.pushdemo.common.Constant;
import com.example.pushdemo.model.AppInfo;
import com.example.pushdemo.model.DeviceInfo;
import com.example.pushdemo.model.PushConfig;
import com.example.pushdemo.model.PushInfo;
import com.example.pushdemo.model.PushRecord;
import com.example.pushdemo.model.PushResult;
import com.example.pushdemo.model.SingleMessage;
import com.example.pushdemo.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.Objects;

/**
 * @author 林杰炜 Linjw
 * @Title 极光推送实现
 * @date 2020/9/30 14:55
 */
@Slf4j
public class JPushHandler extends BasePushHandler {

    private volatile static JPushHandler jPushHandler;
    private static final short JPUSH_CHANNEL = Constant.PUSH_CHANNEL_JPUSH;

    public static JPushHandler getInstance() {
        if (Objects.isNull(jPushHandler)) {
            synchronized (JPushHandler.class) {
                if (Objects.isNull(jPushHandler)) {
                    jPushHandler = new JPushHandler();
                }
            }
        }
        return jPushHandler;
    }

    @Override
    PushResult handler(String no, SingleMessage singleMessage, PushRecord pushRecord, int passThrough, AppInfo appInfo, PushConfig pushConfig, String pushToken) {
        JPushClient jPushClient = new JPushClient(pushConfig.getServiceMasterSecret(), pushConfig.getServiceAppKey(), null, ClientConfig.getInstance());
        PushPayload payload = PushPayload.newBuilder()
                .setPlatform(Platform.android())
                .setAudience(Audience.registrationId(""))
                .setMessage(Message.newBuilder()
                        .setMsgContent(singleMessage.getAlertMsg())
                        .setTitle(singleMessage.getTitle())
                        .addExtras(JsonUtils.readValue(JsonUtils.getJson(singleMessage.getTransmissionContent()), Map.class)).build())
                .setOptions(Options.newBuilder().setTimeToLive(3600).build())
                .build();
        try {
            cn.jpush.api.push.PushResult result = jPushClient.sendPush(payload);
            if (result.isResultOK()) {
                return PushResult.builder().success(true).messageId(singleMessage.getMsgId()).reason(result.getOriginalContent()).build();
            } else {
                return PushResult.builder().success(false).reason(result.toString()).build();
            }
        } catch (Throwable e) {
            return PushResult.builder().success(false).reason("推送失败").build();
        }
    }

    @Override
    PushInfo getPushInfo(DeviceInfo deviceInfo) {
        return CacheHandler.getPushInfo(deviceInfo, JPUSH_CHANNEL);
    }
}

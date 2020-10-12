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
import com.example.pushdemo.utils.JsonUtils;
import com.google.common.collect.Lists;
import com.meizu.push.sdk.constant.ExtraParam;
import com.meizu.push.sdk.server.IFlymePush;
import com.meizu.push.sdk.server.constant.PushResponseCode;
import com.meizu.push.sdk.server.constant.ResultPack;
import com.meizu.push.sdk.server.model.push.VarnishedMessage;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author 林杰炜 Linjw
 * @Title 魅族推送实现类
 * @date 2020/9/29 10:01
 */
@Slf4j
public class MeiZuPushHandler extends BasePushHandler {

    private volatile static MeiZuPushHandler meiZuPushHandler;
    private final static short MEI_ZU_PUSH_CHANNEL = Constant.PUSH_CHANNEL_MEIZU;

    private MeiZuPushHandler() {
    }

    public static MeiZuPushHandler getInstance() {
        if (Objects.isNull(meiZuPushHandler)) {
            synchronized (MeiZuPushHandler.class) {
                if (Objects.isNull(meiZuPushHandler)) {
                    meiZuPushHandler = new MeiZuPushHandler();
                }
            }
        }
        return meiZuPushHandler;
    }

    @Override
    PushResult handler(String no, SingleMessage singleMessage, PushRecord pushRecord, int passThrough, AppInfo appInfo, PushConfig pushConfig, String pushToken) {
        IFlymePush push = new IFlymePush(pushConfig.getServiceAppSecret());
        VarnishedMessage message = new VarnishedMessage.Builder()
                .appId(Long.valueOf(pushConfig.getServiceAppId()))
                .title(singleMessage.getTitle())
                .content(singleMessage.getAlertMsg())
                .clickType(3)
                .customAttribute(JsonUtils.getJson(singleMessage.getTransmissionContent()))
                .validTime(1)
                .extra(ExtraParam.CALLBACK.getKey(), "")
                .extra(ExtraParam.CALLBACK_PARAM.getKey(), "")
                .build();
        List<String> pushIds = Lists.newArrayList(pushToken);
        try {
            ResultPack<com.meizu.push.sdk.server.model.push.PushResult> result = push.pushMessage(message, pushIds, 0);
            if (result.isSucceed()) {
                com.meizu.push.sdk.server.model.push.PushResult meizuResult = result.value();
                Map<String, List<String>> respTarget = meizuResult.getRespTarget();
                if (respTarget.isEmpty()) {
                    return PushResult.builder().success(true).messageId(singleMessage.getMsgId()).reason(JsonUtils.getJson(meizuResult)).build();
                } else {
                    if (respTarget.containsKey(PushResponseCode.RSP_UNSUBSCRIBE_PUSHID)
                            || respTarget.containsKey(PushResponseCode.RSP_INVALID_PUSHID)
                            || respTarget.containsKey(PushResponseCode.RSP_OFF_PUSHID)) {
                        return PushResult.builder().success(false).reason("PushToken无效, " + JsonUtils.getJson(result)).build();
                    } else {
                        return PushResult.builder().success(false).reason(JsonUtils.getJson(result)).build();
                    }
                }
            } else {
                return PushResult.builder().success(false).reason(JsonUtils.getJson(result)).build();
            }
        } catch (IOException e) {
            return PushResult.builder().success(false).reason("魅族推送失败: " + e.getMessage()).build();
        }
    }

    @Override
    PushInfo getPushInfo(DeviceInfo deviceInfo) {
        return CacheHandler.getPushInfo(deviceInfo, MEI_ZU_PUSH_CHANNEL);
    }
}

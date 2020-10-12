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
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.vivo.push.message.Extra;
import com.vivo.push.message.Message;
import com.vivo.push.response.VivoResult;
import com.vivo.push.server.Sender;
import com.vivo.push.server.VivoApp;
import com.vivo.push.util.InitVivoAppUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;

/**
 * @author 林杰炜 Linjw
 * @Title Vivo推送实现
 * @date 2020/9/29 11:17
 */
@Slf4j
public class VivoPushHandler extends BasePushHandler {

    private volatile static VivoPushHandler vivoPushHandler;
    private static final short VIVO_PUSH_CHANNEL = Constant.PUSH_CHANNEL_VIVO;
    private static Cache<String, Sender> senderCache = CacheBuilder.newBuilder().build();

    public static VivoPushHandler getInstance() {
        if (Objects.isNull(vivoPushHandler)) {
            synchronized (VivoPushHandler.class) {
                if (Objects.isNull(vivoPushHandler)) {
                    vivoPushHandler = new VivoPushHandler();
                }
            }
        }
        return vivoPushHandler;
    }

    @Override
    PushResult handler(String no, SingleMessage singleMessage, PushRecord pushRecord, int passThrough, AppInfo appInfo, PushConfig pushConfig, String pushToken) {
        try {
            Sender sender = senderCache.get(pushConfig.getServiceAppId(), () -> {
                VivoApp app = InitVivoAppUtils.initializeApp(Integer.parseInt(pushConfig.getServiceAppId()), pushConfig.getServiceAppKey(), pushConfig.getServiceAppSecret());
                return Sender.getInstance(app);
            });
            Message message = Message.builder()
                    .regId(pushToken)
                    .notifyType(1)
                    .title(singleMessage.getTitle())
                    .content(singleMessage.getAlertMsg())
                    .skipType(3)
                    .skipContent(JsonUtils.getJson(singleMessage.getTransmissionContent()))
                    .networkType(-1)
                    .timeToLive(3600)
                    .requestId(singleMessage.getMsgId())
                    .extra(Extra.builder()
                            .callback("")
                            .callbackParam("")
                            .build())
                    .build();
            VivoResult result = sender.sendSingleMessage(message);
            if (Objects.nonNull(result)) {
                int resultCode = result.getResult();
                if (0 == resultCode) {
                    return PushResult.builder().success(true).messageId(singleMessage.getMsgId()).reason(JsonUtils.getJson(result)).build();
                } else if (10070 == resultCode) {
                    return PushResult.builder().success(false).reason("Vivo推送达到日上限").build();
                } else if (10032 == resultCode) {
                    return PushResult.builder().success(false).reason("PushToken无效 " + JsonUtils.getJson(result)).build();
                } else {
                    return PushResult.builder().success(false).reason(JsonUtils.getJson(result)).build();
                }
            } else {
                return PushResult.builder().success(false).reason("Vivo推送失败").build();
            }
        } catch (Exception e) {
            return PushResult.builder().success(false).reason("Vivo推送失败: " + e.getMessage()).build();
        }
    }

    @Override
    PushInfo getPushInfo(DeviceInfo deviceInfo) {
        return CacheHandler.getPushInfo(deviceInfo, VIVO_PUSH_CHANNEL);
    }
}

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
import com.example.pushdemo.ons.OnsConsumerListener;
import com.example.pushdemo.utils.JsonUtils;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.huawei.push.android.AndroidNotification;
import com.huawei.push.android.BadgeNotification;
import com.huawei.push.android.ClickAction;
import com.huawei.push.message.AndroidConfig;
import com.huawei.push.message.Message;
import com.huawei.push.messaging.HuaweiApp;
import com.huawei.push.messaging.HuaweiMessaging;
import com.huawei.push.response.SendResponse;
import com.huawei.push.util.InitAppUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;

/**
 * @author 林杰炜 Linjw
 * @Title hms推送类
 * @date 2020/5/22 17:35
 */
@Slf4j
public class HMSPushHandler extends BasePushHandler {

    private volatile static HMSPushHandler hmsPushHandler;
    public final static short HMS_PUSH_CHANNEL = Constant.PUSH_CHANNEL_HMS;
    private static Cache<String, HuaweiMessaging> huaweiMessagingCache = CacheBuilder.newBuilder().build();

    private HMSPushHandler() {
    }

    public static HMSPushHandler getInstance() {
        if (Objects.isNull(hmsPushHandler)) {
            synchronized (HMSPushHandler.class) {
                if (Objects.isNull(hmsPushHandler)) {
                    hmsPushHandler = new HMSPushHandler();
                }
            }
        }
        return hmsPushHandler;
    }

    @Override
    PushResult handler(String no, SingleMessage singleMessage, PushRecord pushRecord, int passThrough, AppInfo appInfo, PushConfig pushConfig, String pushToken) {
        try {
            String intent = JsonUtils.getJson(singleMessage.getTransmissionContent());
            String badgeClass = "APP主入口Class";
            String biTag = "";

            AndroidNotification androidNotification = AndroidNotification.builder()
                    .title(singleMessage.getTitle())
                    .body(singleMessage.getAlertMsg())
                    .clickAction(ClickAction.builder()
                            .type(1)
                            .intent(intent)
                            .build())
                    .style(0)
                    .badge(BadgeNotification.builder()
                            .addNum(1)
                            .badgeClass(badgeClass)
                            .build())
                    .build();

            AndroidConfig androidConfig = AndroidConfig.builder()
                    .collapseKey(-1)
                    .notification(androidNotification)
                    .biTag(biTag)
                    .ttl("1")
                    .build();

            Message message = Message.builder()
                    .token(pushToken)
                    .android(androidConfig)
                    .build();

            HuaweiMessaging huaweiMessaging = huaweiMessagingCache.get(pushConfig.getServiceAppKey(), () -> {
                HuaweiApp app = InitAppUtils.initializeApp(pushConfig.getServiceAppKey(), pushConfig.getServiceAppSecret());
                return HuaweiMessaging.getInstance(app);
            });
            SendResponse response = huaweiMessaging.sendMessage(message);

            if ("80000000".equals(response.getCode())) {
                return PushResult.builder().success(true).messageId(singleMessage.getMsgId()).reason(JsonUtils.getJson(response)).build();
            } else if ("80300007".equals(response.getCode())) {
                return PushResult.builder().success(false).reason("pushToken无效 " + JsonUtils.getJson(response)).build();
            } else {
                return PushResult.builder().success(false).reason(JsonUtils.getJson(response)).build();
            }
        } catch (Exception e) {
            log.error("{}, 执行失败", OnsConsumerListener.getThreadLocal().get(), e);
            return PushResult.builder().success(false).reason("推送失败 " + e.getMessage()).build();
        }
    }

    @Override
    PushInfo getPushInfo(DeviceInfo deviceInfo) {
        return CacheHandler.getPushInfo(deviceInfo, HMS_PUSH_CHANNEL);
    }
}

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
import com.xiaomi.push.sdk.ErrorCode;
import com.xiaomi.xmpush.server.Constants;
import com.xiaomi.xmpush.server.Message;
import com.xiaomi.xmpush.server.Result;
import com.xiaomi.xmpush.server.Sender;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

/**
 * @author 林杰炜 Linjw
 * @Title 小米推送
 * @date 2020/9/27 14:08
 */
@Slf4j
public class MiPushHandler extends BasePushHandler {

    private volatile static MiPushHandler miPushHandler;
    public static final short MI_PUSH_CHANNEL = Constant.PUSH_CHANNEL_XIAOMI;

    private MiPushHandler() {
    }

    public static MiPushHandler getInstance() {
        if (Objects.isNull(miPushHandler)) {
            synchronized (MiPushHandler.class) {
                if (Objects.isNull(miPushHandler)) {
                    miPushHandler = new MiPushHandler();
                }
            }
        }
        return miPushHandler;
    }

    @Override
    PushResult handler(String no, SingleMessage singleMessage, PushRecord pushRecord, int passThrough, AppInfo appInfo, PushConfig pushConfig, String pushToken) {
        Sender sender = new Sender(pushConfig.getServiceAppSecret());
        String title = StringUtils.isNotBlank(singleMessage.getTitle()) ? singleMessage.getTitle() : appInfo.getAppName();

        Message message = new Message.Builder()
                .title(title)
                .description(singleMessage.getAlertMsg())
                .payload(JsonUtils.getJson(singleMessage.getTransmissionContent()))
                .restrictedPackageName(appInfo.getPackageName())
                .passThrough(passThrough)
                .notifyType(1)
                .notifyId(singleMessage.getTransmissionContent().hashCode())
                .timeToLive(1L)
                .extra(Constants.EXTRA_PARAM_NOTIFY_FOREGROUND, "1")
                .extra("flow_control", "4000")
                .extra("callback", "")
                .extra("callback.param", "")
                .extra("callback.type", "19")
                .build();
        try {
            Result miResult = sender.send(message, pushToken, 0);
            if (miResult.getErrorCode() == ErrorCode.Success) {
                log.info("{} 米推成功: taskId: {}", no, miResult.getMessageId());
                return PushResult.builder().success(true).messageId(singleMessage.getMsgId()).reason(JsonUtils.getJson(miResult)).build();
            } else {
                log.info("{} 米推失败, 原因: {}", no, miResult.getReason());
                return PushResult.builder().success(false).reason(miResult.getReason()).build();
            }
        } catch (Exception e) {
            log.error("{} 执行米推失败", no, e);
            return PushResult.builder().success(false).reason("米推失败: " + e.getMessage()).build();
        }
    }

    @Override
    PushInfo getPushInfo(DeviceInfo deviceInfo) {
        log.info("获取小米PushInfo");
        return CacheHandler.getPushInfo(deviceInfo, MI_PUSH_CHANNEL);
    }
}

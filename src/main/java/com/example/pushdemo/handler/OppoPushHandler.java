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
import com.oppo.push.server.Notification;
import com.oppo.push.server.Result;
import com.oppo.push.server.Sender;
import com.oppo.push.server.Target;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;

/**
 * @author 林杰炜 Linjw
 * @Title OPPO推送实现
 * @date 2020/9/29 10:43
 */
@Slf4j
public class OppoPushHandler extends BasePushHandler {

    private volatile static OppoPushHandler oppoPushHandler;
    private static final short OPPO_PUSH_CHANNEL = Constant.PUSH_CHANNEL_OPPO;

    private OppoPushHandler() {
    }

    public static OppoPushHandler getInstance() {
        if (Objects.isNull(oppoPushHandler)) {
            synchronized (OppoPushHandler.class) {
                if (Objects.isNull(oppoPushHandler)) {
                    oppoPushHandler = new OppoPushHandler();
                }
            }
        }
        return oppoPushHandler;
    }


    @Override
    PushResult handler(String no, SingleMessage singleMessage, PushRecord pushRecord, int passThrough, AppInfo appInfo, PushConfig pushConfig, String pushToken) {
        try {
            Sender sender = new Sender(pushConfig.getServiceAppKey(), pushConfig.getServiceMasterSecret());
            Notification notification = new Notification();
            notification.setTitle(singleMessage.getTitle());
            notification.setSubTitle("");
            notification.setContent(singleMessage.getAlertMsg());
            notification.setAppMessageId(singleMessage.getMsgId());
            notification.setClickActionType(1);
            notification.setClickActionActivity("");
            notification.setChannelId("");
            notification.setActionParameters(JsonUtils.getJson(singleMessage.getTransmissionContent()));
            notification.setShowTimeType(0);
            notification.setOffLine(true);
            notification.setOffLineTtl(1);
            notification.setTimeZone("GMT+08:00");
            notification.setNetworkType(0);
            notification.setCallBackUrl("");
            notification.setCallBackParameter("");

            Target target = Target.build(pushToken);
            Result result = sender.unicastNotification(notification, target);
            if (0 == result.getReturnCode().getCode()) {
                return PushResult.builder().success(true).messageId(singleMessage.getMsgId()).reason(JsonUtils.getJson(result)).build();
            } else {
                return PushResult.builder().success(false).reason(JsonUtils.getJson(result)).build();
            }
        } catch (Exception e) {
            return PushResult.builder().success(false).reason("OPPO推送失败: " + e.getMessage()).build();
        }
    }

    @Override
    PushInfo getPushInfo(DeviceInfo deviceInfo) {
        return CacheHandler.getPushInfo(deviceInfo, OPPO_PUSH_CHANNEL);
    }
}

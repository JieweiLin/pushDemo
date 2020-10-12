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
import com.gexin.rp.sdk.base.IPushResult;
import com.gexin.rp.sdk.base.payload.APNPayload;
import com.gexin.rp.sdk.http.IGtPush;
import com.gexin.rp.sdk.template.TransmissionTemplate;

import java.util.Objects;

/**
 * @author 林杰炜 Linjw
 * @Title 个推APNs推送实现
 * @date 2020/9/28 10:38
 */
public class GeTuiAPNSPushHandler extends BasePushHandler {

    private volatile static GeTuiAPNSPushHandler geTuiAPNSPushHandler;
    private final static short GE_TUI_APNS_PUSH_CHANNEL = Constant.PUSH_CHANNEL_GETUI_APNS;
    private final static String apiHost = "http://sdk.open.api.igexin.com/apiex.htm";

    private GeTuiAPNSPushHandler() {
    }

    public static GeTuiAPNSPushHandler getInstance() {
        if (Objects.isNull(geTuiAPNSPushHandler)) {
            synchronized (GeTuiAPNSPushHandler.class) {
                if (Objects.isNull(geTuiAPNSPushHandler)) {
                    geTuiAPNSPushHandler = new GeTuiAPNSPushHandler();
                }
            }
        }
        return geTuiAPNSPushHandler;
    }

    @Override
    PushResult handler(String no, SingleMessage singleMessage, PushRecord pushRecord, int passThrough, AppInfo appInfo, PushConfig pushConfig, String pushToken) {
        IGtPush push = new IGtPush(apiHost, pushConfig.getServiceAppKey(), pushConfig.getServiceMasterSecret());
        TransmissionTemplate template = new TransmissionTemplate();
        template.setAppId(pushConfig.getServiceAppId());
        template.setAppkey(pushConfig.getServiceAppKey());
        template.setTransmissionContent("");
        template.setTransmissionType(2);
        APNPayload payload = new APNPayload();
        payload.setAutoBadge("+1");
        payload.setContentAvailable(0);
        payload.setSound("default");
        payload.addCustomMsg("msg", singleMessage.getTransmissionContent());
        payload.setAlertMsg(new APNPayload.DictionaryAlertMsg().setTitle(singleMessage.getTitle()).setBody(singleMessage.getAlertMsg()));
        template.setAPNInfo(payload);
        com.gexin.rp.sdk.base.impl.SingleMessage message = new com.gexin.rp.sdk.base.impl.SingleMessage();
        message.setOffline(true);
        message.setOfflineExpireTime(1L);
        message.setData(template);
        message.setPushNetWorkType(0);
        IPushResult result = push.pushAPNMessageToSingle(pushConfig.getServiceAppId(), pushToken, message);
        if (Objects.nonNull(result)) {
            String resultCode = (String) result.getResponse().get("result");
            if ("ok".equals(resultCode)) {
                return PushResult.builder().success(true).messageId(singleMessage.getMsgId()).reason(result.getResponse().toString()).build();
            } else if ("PushTotalNumOverLimit".equals(resultCode)) {
                CacheHandler.setReachLimit(GE_TUI_APNS_PUSH_CHANNEL, singleMessage.getDeviceInfo().getAppId());
                return PushResult.builder().success(false).reason("个推APNs推送达到日上限").build();
            } else {
                return PushResult.builder().success(false).reason("IOS个推推送失败, 失败原因: " + result.getResponse().toString()).build();
            }
        } else {
            return PushResult.builder().success(false).reason("服务器无响应").build();
        }
    }

    @Override
    PushInfo getPushInfo(DeviceInfo deviceInfo) {
        return CacheHandler.getPushInfo(deviceInfo, GE_TUI_APNS_PUSH_CHANNEL);
    }
}

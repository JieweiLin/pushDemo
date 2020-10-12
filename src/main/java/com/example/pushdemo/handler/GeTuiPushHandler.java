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
import com.gexin.rp.sdk.base.IIGtPush;
import com.gexin.rp.sdk.base.IPushResult;
import com.gexin.rp.sdk.base.impl.Target;
import com.gexin.rp.sdk.base.notify.Notify;
import com.gexin.rp.sdk.dto.GtReq;
import com.gexin.rp.sdk.http.IGtPush;
import com.gexin.rp.sdk.template.TransmissionTemplate;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;

/**
 * @author 林杰炜 Linjw
 * @Title 个推推送实现
 * @date 2020/5/21 17:06
 */
@Slf4j
public class GeTuiPushHandler extends BasePushHandler {

    private volatile static GeTuiPushHandler geTuiPushHandler;
    public final static short GE_TUI_PUSH_CHANNEL = Constant.PUSH_CHANNEL_GETUI;
    private final static String apiHost = "http://sdk.open.api.igexin.com/apiex.htm";

    private GeTuiPushHandler() {
    }

    public static GeTuiPushHandler getInstance() {
        if (Objects.isNull(geTuiPushHandler)) {
            synchronized (GeTuiPushHandler.class) {
                if (Objects.isNull(geTuiPushHandler)) {
                    geTuiPushHandler = new GeTuiPushHandler();
                }
            }
        }
        return geTuiPushHandler;
    }

    @Override
    PushResult handler(String no, SingleMessage singleMessage, PushRecord pushRecord, int passThrough, AppInfo appInfo, PushConfig pushConfig, String pushToken) {
        IIGtPush push = new IGtPush(apiHost, pushConfig.getServiceAppKey(), pushConfig.getServiceMasterSecret());
        com.gexin.rp.sdk.base.impl.SingleMessage message = new com.gexin.rp.sdk.base.impl.SingleMessage();

        TransmissionTemplate template = new TransmissionTemplate();
        template.setAppId(pushConfig.getServiceAppId());
        template.setAppkey(pushConfig.getServiceAppKey());
        template.setTransmissionContent(JsonUtils.getJson(singleMessage.getTransmissionContent()));
        template.setTransmissionType(2);

        Notify notify = new Notify();
        notify.setTitle(singleMessage.getTitle());
        notify.setContent(singleMessage.getAlertMsg());
        notify.setType(GtReq.NotifyInfo.Type._intent);
        notify.setIntent("");

        template.set3rdNotifyInfo(notify);
        message.setData(template);
        message.setOffline(true);
        message.setOfflineExpireTime(1L);

        Target target = new Target();
        target.setAppId(pushConfig.getServiceAppId());
        target.setClientId(pushToken);

        IPushResult result = push.pushMessageToSingle(message, target);

        if (Objects.nonNull(result)) {
            String resultCode = (String) result.getResponse().get("result");
            if ("ok".equals(resultCode)) {
                log.info("{} 个推推送成功, resultCode: {}, resp: {}", no, resultCode, result.getResponse());
                return PushResult.builder().success(true).messageId(singleMessage.getMsgId()).reason(result.getResponse().toString()).build();
            } else if ("TokenMD5NoUsers".equals(resultCode)) {
                log.info("{} pushToken无效: {}", no, result.getResponse().toString());
                return PushResult.builder().success(false).reason("pushToken无效 " + result.getResponse().toString()).build();
            } else {
                log.warn("{} 个推失败: {}, 失败原因: {}", no, JsonUtils.getJson(singleMessage), result.getResponse().toString());
                return PushResult.builder().success(false).reason(result.getResponse().toString()).build();
            }
        } else {
            log.warn("服务器无响应");
            return PushResult.builder().success(false).reason("服务器无响应").build();
        }
    }

    @Override
    PushInfo getPushInfo(DeviceInfo deviceInfo) {
        log.info("getPushInfo: {}", GE_TUI_PUSH_CHANNEL);
        return CacheHandler.getPushInfo(deviceInfo, GE_TUI_PUSH_CHANNEL);
    }
}

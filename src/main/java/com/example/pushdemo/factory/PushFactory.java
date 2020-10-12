package com.example.pushdemo.factory;

import com.example.pushdemo.cache.CacheHandler;
import com.example.pushdemo.common.Constant;
import com.example.pushdemo.common.DateUtils;
import com.example.pushdemo.config.PushServerConfig;
import com.example.pushdemo.handler.GeTuiAPNSPushHandler;
import com.example.pushdemo.handler.GeTuiPushHandler;
import com.example.pushdemo.handler.HMSPushHandler;
import com.example.pushdemo.handler.JPushHandler;
import com.example.pushdemo.handler.MeiZuPushHandler;
import com.example.pushdemo.handler.MiPushHandler;
import com.example.pushdemo.handler.OppoPushHandler;
import com.example.pushdemo.handler.UMengPushHandler;
import com.example.pushdemo.handler.VivoPushHandler;
import com.example.pushdemo.model.AppInfo;
import com.example.pushdemo.model.DeviceInfo;
import com.example.pushdemo.model.PushRecord;
import com.example.pushdemo.model.PushRule;
import com.example.pushdemo.model.SingleMessage;
import com.example.pushdemo.ons.OnsConsumerListener;
import com.example.pushdemo.utils.JsonUtils;
import com.google.common.collect.Sets;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * @author 林杰炜 Linjw
 * @Title 推送处理
 * @date 2020/5/22 18:06
 */
@Log4j2
public class PushFactory {

    @Autowired
    private static PushServerConfig pushServerConfig;

    public static void pushSingle(SingleMessage singleMessage, PushRecord pushRecord) {
        if (Objects.isNull(singleMessage)) {
            return;
        }

        String no = OnsConsumerListener.getThreadLocal().get();
        String msgId = singleMessage.getMsgId();

        //1、是否重复投递
        if (CacheHandler.isRepeat(msgId)) {
            pushRecord.setReason("重复投递");
            return;
        }

        //2、是否在拒绝时间段内
        String denyStartTime = pushServerConfig.getDenyStartTime();
        String denyEndTime = pushServerConfig.getDenyEndTime();
        String start = DateUtils.getStringDateShort() + " " + denyStartTime;
        String end = DateUtils.getStringDateShort() + " " + denyEndTime;
        if (DateUtils.strToDateLong(start).getTime() < System.currentTimeMillis() &&
                DateUtils.strToDateLong(end).getTime() > System.currentTimeMillis()) {
            log.info("{} 消息不在推送允许时间投递, 放弃 singleMessage: {}", no, JsonUtils.getJson(singleMessage));
            return;
        }

        //3、是否消息延迟
        String delayTime = pushServerConfig.getDelayTime();
        Integer time = Integer.parseInt(delayTime);
        if (Math.abs(System.currentTimeMillis() - singleMessage.getSendTime().getTime()) > 60 * time * 1000) {
            log.info("{} 消息延迟过长, 放弃: {}", no, msgId);
            return;
        }

        //4、获取AppInfo
        AppInfo appInfo = CacheHandler.getAppInfo(singleMessage.getDeviceInfo().getAppId());
        if (Objects.isNull(appInfo)) {
            log.warn("未找到应用信息 appId: {}", singleMessage.getDeviceInfo().getAppId());
            return;
        }

        //5、黑名单设备不推送
        if (CacheHandler.isForbidDevice(singleMessage.getDeviceInfo().getDeviceId())) {
            log.info("{} 黑名单设备: {}, 不推送, 消息内容: {}", no, singleMessage.getDeviceInfo().getDeviceId(), JsonUtils.getJson(singleMessage));
            return;
        }

        //6、推送规则
        PushRule pushRule = CacheHandler.getPushRule(singleMessage.getPushCode());
        Short unit = pushRule.getUnit();
        Integer expTime = 3600;
        String t = "小时";
        if (2 == unit) {
            expTime = (int) ((DateUtils.strToDateLong(DateUtils.getStringDateShort() + " 23:59:59").getTime() - System.currentTimeMillis()) / 1000);
            t = "天";
        }
        Long num = CacheHandler.getPushNum(singleMessage.getDeviceInfo().getDeviceId(), singleMessage.getPushCode(), expTime);
        if (num.intValue() > pushRule.getNumTimes()) {
            log.info("{} {}-{} 超出推送上限{}, 当前第{}次, 当前规则按:{}", no, singleMessage.getDeviceInfo().getDeviceId(), singleMessage.getPushCode(), pushRule.getNumTimes(), num, t);
            return;
        }

        //7、区分Android或IOS
        if (Constant.APP_TYPE_ANDROID.equals(appInfo.getAppType())) {

            //获取DeviceInfo
            DeviceInfo deviceInfo = CacheHandler.getDeviceInfo(singleMessage.getDeviceInfo());
            if (Objects.isNull(deviceInfo)) {
                log.info("{} {} - {} 无设备信息", no, singleMessage.getDeviceInfo().getDeviceId(), singleMessage.getDeviceInfo().getAppId());
                return;
            }

            Set<Short> pushChannels = Sets.newHashSet(JsonUtils.readJson2Array(deviceInfo.getPushChannels(), Short.class));
            if (pushChannels.size() == 1) {
                pushChannels.forEach(channel -> pushChannelSelection(singleMessage, pushRecord, appInfo, 1, channel, null));
            }
        } else if (Constant.APP_TYPE_IOS.equals(appInfo.getAppType())) {

        }
    }

    private static void pushChannelSelection(SingleMessage singleMessage, PushRecord pushRecord, AppInfo appInfo, int passThrough, Short channel, List<Short> tempPushChannels) {
        switch (Constant.getPushChannelEnum(channel)) {
            case GE_TUI:
                GeTuiPushHandler.getInstance().doPush(singleMessage, pushRecord, appInfo, channel, passThrough);
                break;
            case U_MENG:
                UMengPushHandler.getInstance().doPush(singleMessage, pushRecord, appInfo, channel, passThrough);
                break;
            case MI_PUSH:
                MiPushHandler.getInstance().doPush(singleMessage, pushRecord, appInfo, channel, passThrough);
                break;
            case GE_TUI_APNS:
                GeTuiAPNSPushHandler.getInstance().doPush(singleMessage, pushRecord, appInfo, channel, passThrough);
                break;
            case HMS_PUSH:
                HMSPushHandler.getInstance().doPush(singleMessage, pushRecord, appInfo, channel, passThrough);
                break;
            case MEI_ZU_PUSH:
                MeiZuPushHandler.getInstance().doPush(singleMessage, pushRecord, appInfo, channel, passThrough);
                break;
            case OPPO_PUSH:
                OppoPushHandler.getInstance().doPush(singleMessage, pushRecord, appInfo, channel, passThrough);
                break;
            case VIVO_PUSH:
                VivoPushHandler.getInstance().doPush(singleMessage, pushRecord, appInfo, channel, passThrough);
                break;
            case JPUSH:
                JPushHandler.getInstance().doPush(singleMessage, pushRecord, appInfo, channel, passThrough);
                break;

            default:
                break;
        }
    }
}

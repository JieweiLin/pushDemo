package com.example.pushdemo.handler;

import com.example.pushdemo.cache.CacheHandler;
import com.example.pushdemo.model.AppInfo;
import com.example.pushdemo.model.DeviceInfo;
import com.example.pushdemo.model.PushConfig;
import com.example.pushdemo.model.PushInfo;
import com.example.pushdemo.model.PushRecord;
import com.example.pushdemo.model.PushResult;
import com.example.pushdemo.model.SingleMessage;
import com.example.pushdemo.ons.OnsConsumerListener;
import com.example.pushdemo.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StopWatch;

import java.util.Objects;

/**
 * @author 林杰炜 Linjw
 * @Title 基础推送处理
 * @date 2020/5/21 16:04
 */
@Slf4j
public abstract class BasePushHandler {

    /**
     * 推送第三方处理逻辑
     *
     * @param no
     * @param singleMessage
     * @param pushRecord
     * @param passThrough
     * @param appInfo
     * @param pushConfig
     * @param pushToken
     * @return
     */
    abstract PushResult handler(String no, SingleMessage singleMessage, PushRecord pushRecord, int passThrough, AppInfo appInfo, PushConfig pushConfig, String pushToken);

    /**
     * 获取设备推送信息
     *
     * @param deviceInfo
     * @return
     */
    abstract PushInfo getPushInfo(DeviceInfo deviceInfo);

    public final PushResult doPush(SingleMessage singleMessage, PushRecord pushRecord, AppInfo appInfo, Short pushChannel, int passThrough) {
        String no = OnsConsumerListener.getThreadLocal().get();
        DeviceInfo deviceInfo = singleMessage.getDeviceInfo();
        PushResult pushResult = null;

        StopWatch watch = new StopWatch();
        try {
            watch.start(no + "--->获取PushConfig");
            PushConfig pushConfig = CacheHandler.getPushConfig(deviceInfo.getAppId(), pushChannel);
            if (Objects.isNull(pushConfig)) {
                log.warn("未找到第三方配置信息 appId: {}, pushChannel: {}", deviceInfo.getAppId(), pushChannel);
                pushRecord.setReason("未找到第三方配置信息");
                return PushResult.builder().success(false).reason("未找到第三方配置信息").build();
            }

            PushInfo pushInfo = getPushInfo(deviceInfo);
            if (Objects.isNull(pushInfo)) {
                log.warn("未找到设备推送信息 deviceInfo: {}, pushChannel: {}", JsonUtils.getJson(deviceInfo), pushChannel);
                pushRecord.setReason("未找到设备推送信息");
                return PushResult.builder().success(false).reason("未找到设备推送信息").build();
            }

            pushResult = handler(no, singleMessage, pushRecord, passThrough, appInfo, pushConfig, pushInfo.getDeviceToken());
        } catch (Exception e) {
            log.error("{} 执行推送失败", no, e);
            pushRecord.setReason("提送失败: " + e.getMessage());
        } finally {
            log.info("pushRecord!!!");
        }
        return pushResult;
    }
}

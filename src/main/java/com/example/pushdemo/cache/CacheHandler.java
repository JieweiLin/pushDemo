package com.example.pushdemo.cache;

import com.example.pushdemo.common.DateUtils;
import com.example.pushdemo.model.AppInfo;
import com.example.pushdemo.model.DeviceInfo;
import com.example.pushdemo.model.PushConfig;
import com.example.pushdemo.model.PushInfo;
import com.example.pushdemo.model.PushRule;
import com.example.pushdemo.utils.JsonUtils;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.Maps;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author 林杰炜 Linjw
 * @Title 缓存处理
 * @date 2020/5/22 11:02
 */
@Log4j2
public class CacheHandler {

    private static Map<String, PushConfig> pushConfigs = Maps.newConcurrentMap();
    private static Map<String, AppInfo> appInfos = Maps.newConcurrentMap();
    private static Map<String, PushRule> pushRules = Maps.newConcurrentMap();
    private static Cache<String, Integer> reachLimit = CacheBuilder.newBuilder().initialCapacity(10).expireAfterWrite(1, TimeUnit.DAYS).build();

    static boolean pushConfigLock = false;
    static boolean appInfoLock = false;
    static boolean pushRuleLock = false;

    @Autowired
    static RedisTemplate redisTemplate;

    /**
     * 更新第三方推送平台配置信息
     *
     * @param configs
     */
    public static void updatePushConfig(List<PushConfig> configs) {
        synchronized (pushConfigs) {
            try {
                pushConfigLock = true;
                if (configs.isEmpty()) {
                    return;
                }
                pushConfigs.clear();
                pushConfigs = configs.stream().collect(Collectors.toConcurrentMap(k -> k.getAppId() + "_" + k.getPushChannel(), pushConfig -> pushConfig));
            } finally {
                pushConfigLock = false;
            }
        }
    }

    /**
     * 获取第三方推送平台配置信息
     *
     * @param appId
     * @param pushChannel
     * @return
     */
    public static PushConfig getPushConfig(String appId, Short pushChannel) {
        try {
            if (pushConfigLock) {
                log.info("正在更新配置, 线程进入等待...");
                synchronized (pushConfigs) {
                }
            }
            return pushConfigs.get(appId + "_" + pushChannel);
        } catch (Exception e) {
            log.error("获取第三方推送平台配置信息失败 appId: {}, pushChannel: {}", appId, pushChannel, e);
            return null;
        }
    }

    /**
     * 更新内部应用信息
     *
     * @param apps
     */
    public static void updateAppInfo(List<AppInfo> apps) {
        synchronized (appInfos) {
            try {
                appInfoLock = true;
                if (apps.isEmpty()) {
                    return;
                }
                appInfos.clear();
                appInfos = apps.stream().collect(Collectors.toConcurrentMap(AppInfo::getAppId, Function.identity(), (k1, k2) -> k2));
            } finally {
                appInfoLock = false;
            }
        }
    }

    /**
     * 获取内部应用信息
     *
     * @param appId
     * @return
     */
    public static AppInfo getAppInfo(String appId) {
        try {
            if (appInfoLock) {
                log.info("正在更新配置, 线程进入等待...");
                synchronized (appInfos) {
                }
            }
            return appInfos.get(appId);
        } catch (Exception e) {
            log.error("获取内部应用信息失败 appId: {}", appId, e);
            return null;
        }
    }

    /**
     * 更新推送规则
     *
     * @param rules
     */
    public static void updatePushRule(List<PushRule> rules) {
        synchronized (pushRules) {
            try {
                pushRuleLock = true;
                if (rules.isEmpty()) {
                    return;
                }
                pushRules.clear();
                pushRules = rules.stream().collect(Collectors.toConcurrentMap(PushRule::getPushCode, Function.identity(), (k1, k2) -> k2));
            } finally {
                pushRuleLock = false;
            }
        }
    }

    /**
     * 获取推送规则
     *
     * @param pushCode
     * @return
     */
    public static PushRule getPushRule(String pushCode) {
        try {
            if (pushRuleLock) {
                log.info("正在更新配置, 线程进入等待...");
                synchronized (pushRules) {
                }
            }
            return pushRules.get(pushCode);
        } catch (Exception e) {
            log.error("获取推送规则失败 pushCode: {}", pushCode);
            return null;
        }
    }

    public static PushInfo getPushInfo(DeviceInfo deviceInfo, Short pushChannel) {
        return new PushInfo();
    }

    private static final String repeatKeyFormat = "HasPushId:%s:%s";
    private static final String pushNumKeyFormat = "pushNum:%s:%s";
    private static final String deviceInfoKeyFormat = "di:%s:%s";

    /**
     * 是否重复投递
     *
     * @param msgId
     * @return
     */
    public static boolean isRepeat(String msgId) {
        String key = String.format(repeatKeyFormat, DateUtils.getStringShortToday(), msgId.substring(0, 4));
        return redisTemplate.opsForSet().isMember(key, msgId);
    }

    public static boolean isForbidDevice(String deviceId) {
        return false;
    }

    public static Long getPushNum(String deviceId, String pushCode, Integer expTime) {
        String key = String.format(pushNumKeyFormat, deviceId, pushCode);
        Long num = redisTemplate.opsForValue().increment(key);
        if (1 == num) {
            redisTemplate.expire(key, expTime, TimeUnit.SECONDS);
        }
        return num;
    }

    public static DeviceInfo getDeviceInfo(DeviceInfo deviceInfo) {
        String key = String.format(deviceInfoKeyFormat, deviceInfo.getDeviceId(), deviceInfo.getAppId());
        String cache = (String) redisTemplate.opsForValue().get(key);
        return JsonUtils.readValue(cache, DeviceInfo.class);
    }

    /**
     * 校验是否达到日上限
     *
     * @param pushChannel
     * @param appId
     * @return
     */
    public static boolean isReachLimit(short pushChannel, String appId) {
        String key = String.format("%s:%s:%s", DateUtils.getStringShortToday(), pushChannel, appId);
        return Objects.isNull(reachLimit.getIfPresent(key)) ? false : true;
    }

    /**
     * 达到日上限
     *
     * @param pushChannel
     * @param appId
     */
    public static void setReachLimit(short pushChannel, String appId) {
        String key = String.format("%s:%s:%s", DateUtils.getStringShortToday(), pushChannel, appId);
        reachLimit.put(key, 1);
    }
}

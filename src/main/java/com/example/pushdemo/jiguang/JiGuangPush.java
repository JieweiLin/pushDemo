package com.example.pushdemo.jiguang;

import cn.jiguang.common.ClientConfig;
import cn.jiguang.common.resp.APIConnectionException;
import cn.jiguang.common.resp.APIRequestException;
import cn.jpush.api.JPushClient;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.Message;
import cn.jpush.api.push.model.Options;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.notification.AndroidNotification;
import cn.jpush.api.push.model.notification.IosAlert;
import cn.jpush.api.push.model.notification.IosNotification;
import cn.jpush.api.push.model.notification.Notification;
import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;

import java.util.Map;

/**
 * @author 林杰炜 linjw
 * @date 2018/12/27 10:51
 */
public class JiGuangPush {

    static String masterSecret = "c46bace089f3819182d0d20c";
    static String appKey = "d27d5de054ec9f5dc717ed74";
    static String regId = "";

    public static void main(String[] args) {
        JPushClient jPushClient = new JPushClient(masterSecret, appKey, null, ClientConfig.getInstance());
        //PushPayload payload = buildPushObject();
        PushPayload payload = buildPushObject2();
        try {
            PushResult result = jPushClient.sendPush(payload);
            System.out.println("result:" + JSON.toJSONString(result));
        } catch (APIConnectionException e) {
            e.printStackTrace();
        } catch (APIRequestException e) {
            e.printStackTrace();
        }
    }

    private static PushPayload buildPushObject2() {
        Map<String, String> alert = Maps.newHashMap();
        alert.put("title", "测试标题");
        alert.put("subtitle", "测试推送消息");
        alert.put("body", "测试推送消息");
        alert.put("action-loc-key", "PLAY");
        Map<String, String> extras = Maps.newHashMap();
        extras.put("extras", "测试备注");
        return PushPayload.newBuilder()
                .setPlatform(Platform.all())
                .setAudience(Audience.alias("panf"))
                .setNotification(Notification.newBuilder()
                        .addPlatformNotification(AndroidNotification.newBuilder().setAlert("测试推送消息").addExtras(extras).setTitle("测试标题").build())
                        .addPlatformNotification(IosNotification.newBuilder().setAlert(IosAlert.newBuilder().setTitleAndBody("测试标题", "测试推送消息", "测试推送消息").setActionLocKey("PLAY").build()).addExtras(extras).incrBadge(1).setSound("default").build())
                        .build())
                .setMessage(Message.newBuilder().setTitle("测试推送 消息").setMsgContent("测试推送消息").addExtra("extras", "测试备注").build())
                .setOptions(Options.newBuilder().setSendno(1).setTimeToLive(86400).setApnsProduction(true).build())
                .build();
    }

    private static PushPayload buildPushObject() {
        Map<String, Object> map = Maps.newHashMap();
        Map<String, String> content = Maps.newHashMap();
        content.put("data", "push");
        map.put("content", content);
        return PushPayload.newBuilder()
                .setPlatform(Platform.android())
                .setAudience(Audience.registrationId(regId))
                //.setNotification(Notification.newBuilder().setAlert("alert").addPlatformNotification(AndroidNotification.newBuilder().build()).build())
                .setMessage(Message.newBuilder().setMsgContent("msgContent").setTitle("title").addExtras(content).build())
                .setOptions(Options.newBuilder().setTimeToLive(3600).build())
                .build();
    }
}

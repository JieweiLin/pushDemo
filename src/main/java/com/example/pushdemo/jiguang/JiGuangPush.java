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
import cn.jpush.api.push.model.notification.Notification;
import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;

import java.util.Map;

/**
 * @author 林杰炜 linjw
 * @date 2018/12/27 10:51
 */
public class JiGuangPush {

    static String masterSecret = "27c0599b05f0d947e5c06ebd";
    static String appKey = "af84d3697a0d42ff452f239d";
    //static String regId = "170976fa8adcf2eb5bd";
    static String regId = "1507bfd3f7808dc8c68";

    public static void main(String[] args) {
        JPushClient jPushClient = new JPushClient(masterSecret, appKey, null, ClientConfig.getInstance());
        PushPayload payload = buildPushObject();
        try {
            PushResult result = jPushClient.sendPush(payload);
            System.out.println("result:" + JSON.toJSONString(result));
        } catch (APIConnectionException e) {
            e.printStackTrace();
        } catch (APIRequestException e) {
            e.printStackTrace();
        }
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

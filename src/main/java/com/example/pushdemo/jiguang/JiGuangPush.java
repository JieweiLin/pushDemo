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
import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;

import java.util.Map;

/**
 * @author 林杰炜 linjw
 * @date 2018/12/27 10:51
 */
public class JiGuangPush {

    static String masterSecret = "";
    static String appKey = "";
    static String regId = "";

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

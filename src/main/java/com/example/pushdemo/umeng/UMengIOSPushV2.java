package com.example.pushdemo.umeng;

import org.json.JSONObject;
import push.IOSUnicast;
import push.PushClient;

/**
 * @author 林杰炜 linjw
 * @Title TODO 类描述
 * @Description TODO 详细描述
 * @Copyright 2014-现在 厦门神州鹰掌通家园项目组
 * @date 2019/4/8 14:53
 */
public class UMengIOSPushV2 {
    static String appKey = "";
    static String appMasterSecret = "";
    static String deviceToke = "";

    public static void main(String[] args) throws Exception {
        PushClient client = new PushClient();
        IOSUnicast unicast = new IOSUnicast(appKey, appMasterSecret);
        unicast.setDeviceToken(deviceToke);
        JSONObject alert = new JSONObject();
        alert.put("title", "title");
        alert.put("body", "body");
        unicast.setAlert(alert);
        unicast.setBadge(0);
        unicast.setSound("default");

        unicast.setProductionMode();
        String content = "";
        unicast.setCustomizedField("msg", content);
        client.send(unicast);
    }
}

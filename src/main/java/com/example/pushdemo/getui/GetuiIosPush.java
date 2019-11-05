package com.example.pushdemo.getui;

import com.alibaba.fastjson.JSON;
import com.gexin.rp.sdk.base.IPushResult;
import com.gexin.rp.sdk.base.impl.SingleMessage;
import com.gexin.rp.sdk.base.payload.APNPayload;
import com.gexin.rp.sdk.http.IGtPush;
import com.gexin.rp.sdk.template.TransmissionTemplate;

/**
 * @author 林杰炜 linjw
 * @date 2019/4/2 9:59
 */
public class GetuiIosPush {

    private static String apiHost = "http://sdk.open.api.igexin.com/apiex.htm";
    private static String appKey = "yCB916qL6470SRuwMoRgv9";
    private static String masterSecret = "aY9DN6BBnv5pgwz7nmOXx1";
    private static String appId = "TgnJrR1ykI7aQ0iu0m9N5A";

    public static void main(String[] args) {
        String deviceToke = "1cfaf3fc05fffda7a8e690eacfdb134f081e061426ead2fb8b2113556354f5cf";
        String content = "{\"childid\":\"aZKAMtdOjPKj98kFrj4\",\"createTime\":\"2019-04-02 08:36:35\",\"msgcode\":\"m107\",\"msgid\":\"9806F27E0B184FFAA4D7B5FC07B92BA8\",\"schoolid\":\"GByyPPIpBxWgf5jhley\"}";
        IGtPush push = new IGtPush(apiHost, appKey, masterSecret);
        TransmissionTemplate template = new TransmissionTemplate();
        template.setAppId(appId);
        template.setAppkey(appKey);
        template.setTransmissionContent("");
        template.setTransmissionType(2);
        APNPayload payload = new APNPayload();
        payload.setBadge(1);
        payload.setContentAvailable(0);
        payload.setSound("default");
        payload.addCustomMsg("msg", content);
        payload.setAlertMsg(new APNPayload.SimpleAlertMsg("测试！！！"));
        template.setAPNInfo(payload);
        SingleMessage message = new SingleMessage();
        message.setOffline(true);
        message.setOfflineExpireTime(3600);
        message.setData(template);
        message.setPushNetWorkType(0);
        IPushResult result = push.pushAPNMessageToSingle(appId, deviceToke, message);
        System.out.println(JSON.toJSONString(result));
    }
}

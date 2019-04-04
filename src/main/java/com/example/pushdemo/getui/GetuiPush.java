package com.example.pushdemo.getui;

import com.alibaba.fastjson.JSON;
import com.gexin.rp.sdk.base.IIGtPush;
import com.gexin.rp.sdk.base.IPushResult;
import com.gexin.rp.sdk.base.impl.SingleMessage;
import com.gexin.rp.sdk.base.impl.Target;
import com.gexin.rp.sdk.http.IGtPush;
import com.gexin.rp.sdk.template.TransmissionTemplate;

/**
 * @author 林杰炜 linjw
 * @Title TODO 类描述
 * @Description TODO 详细描述
 * @Copyright 2014-现在 厦门神州鹰掌通家园项目组
 * @date 2018/12/20 15:38
 */
public class GetuiPush {
    private static String apihost = "http://sdk.open.api.igexin.com/apiex.htm";
    private static String appKey = "yCB916qL6470SRuwMoRgv9";
    private static String masterSecret = "aY9DN6BBnv5pgwz7nmOXx1";
    private static String appId = "TgnJrR1ykI7aQ0iu0m9N5A";

    public static void main(String[] args) {
        String deviceToken = "ebcd7210c453c34577693665020a104e";
        String content = "{\"groupid\":\"0B520D8F1B2A4FF3A0A2622E6CE84CAD\",\"message\":\"爸爸林杰炜于08点21分入校，请关注林杰炜是否已入校！\",\"msgid\":\"428742B035B84E668CD6FCA296D7DE47\",\"msgtext\":{\" childid\":\"1f7a9485eb11e632ac2d\",\"createTime\":\"2018-12-21 08:21:42\",\"msgcode\":\"m107\",\"schoolid\":\"GByyPPIpBxWgf5jhley\"},\"pushtype\":\"1\"}";
        IIGtPush push = new IGtPush(apihost, appKey, masterSecret);
        SingleMessage message = new SingleMessage();
        TransmissionTemplate template = new TransmissionTemplate();
        template.setAppId(appId);
        template.setAppkey(appKey);
        template.setTransmissionContent(content);
        template.setTransmissionType(2);
        message.setData(template);
        message.setOffline(true);
        message.setOfflineExpireTime(60*60*1000L);
        Target target = new Target();
        target.setAppId(appId);
        target.setClientId(deviceToken);
        IPushResult result = push.pushMessageToSingle(message, target);
        System.out.println(JSON.toJSONString(result));
    }
}

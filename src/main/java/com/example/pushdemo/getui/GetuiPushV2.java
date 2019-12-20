package com.example.pushdemo.getui;

import com.alibaba.fastjson.JSON;
import com.gexin.rp.sdk.base.IIGtPush;
import com.gexin.rp.sdk.base.IPushResult;
import com.gexin.rp.sdk.base.impl.SingleMessage;
import com.gexin.rp.sdk.base.impl.Target;
import com.gexin.rp.sdk.base.notify.Notify;
import com.gexin.rp.sdk.dto.GtReq;
import com.gexin.rp.sdk.http.IGtPush;
import com.gexin.rp.sdk.template.TransmissionTemplate;
import com.google.common.collect.Maps;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

/**
 * @author 林杰炜 linjw
 * @date 2019/1/10 20:26
 */
public class GetuiPushV2 {
    private static String apihost = "http://sdk.open.api.igexin.com/apiex.htm";
    private static String appKey = "";
    private static String masterSecret = "";
    private static String appId = "";

    public static void main(String[] args) throws UnsupportedEncodingException {
        IIGtPush push = new IGtPush(apihost, appKey, masterSecret);
        SingleMessage message = new SingleMessage();
        TransmissionTemplate template = new TransmissionTemplate();
        template.setAppId(appId);
        template.setAppkey(appKey);
        Map<String, Object> map = Maps.newHashMap();
        Map<String, String> content = Maps.newHashMap();
        content.put("data", "push");
        map.put("content", content);
        //template.setTransmissionContent(JSON.toJSONString(map, new SerializerFeature[]{SerializerFeature.WriteDateUseDateFormat, SerializerFeature.DisableCircularReferenceDetect}));
        template.setTransmissionContent("{\"groupid\":\"6BEF693944584F6FAEC76209F216A68D\",\"message\":\"爸爸林杰炜于10点09分入校，请关注林杰炜是否已入校！\",\"msgid\":\"8909D3EAEB0A4DAEA7A3316AE44F93DS\",\"msgtext\":{\"childid\":\"1f7a9485eb11e632ac2d\",\"createTime\":\"2019-01-12 10:09:02\",\"msgcode\":\"m107\",\"schoolid\":\"GByyPPIpBxWgf5jhley\"},\"pushtype\":\"1\"}");
        template.setTransmissionType(2);

        Notify notify = new Notify();
        notify.setTitle("title1");
        notify.setContent("content");
        notify.setType(GtReq.NotifyInfo.Type._intent);
        //notify.setIntent("intent:#Intent;launchFlags=0x10000000;package=com.seebaby;component=com.seebaby/com.szy.szypush.getui.GetuiTransferActivity;S.content=%7B%22data%22%3A%22push%22%7D;end");
        //String str = "intent:#Intent;launchFlags=0x10000000;package=com.seebaby;component=com.seebaby/com.szy.szypush.getui.GetuiTransferActivity;S.content=" + URLEncoder.encode(JSON.toJSONString(content), "UTF-8") + ";end";
        //intent:#Intent;launchFlags=0x10000000;package=com.seebaby;component=com.seebaby/com.szy.szypush.getui.GetuiTransferActivity;S.content={"data":"push"};end
        notify.setIntent("intent:#Intent;launchFlags=0x10000000;package=com.seebaby;component=com.seebaby/com.szy.szypush.getui.GetuiTransferActivity;S.custommsg=" + URLEncoder.encode(JSON.toJSONString(content), "UTF-8") + ";end");
        template.set3rdNotifyInfo(notify);
        message.setData(template);
        message.setOffline(true);
        message.setOfflineExpireTime(3600);
        Target target = new Target();
        target.setAppId(appId);
        target.setClientId("");

        IPushResult result = push.pushMessageToSingle(message, target);
        System.out.println("time:" + System.currentTimeMillis() + " " + JSON.toJSONString(result));
    }
}

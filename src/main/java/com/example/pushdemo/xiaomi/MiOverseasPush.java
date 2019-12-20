package com.example.pushdemo.xiaomi;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import com.xiaomi.xmpush.server.Constants;
import com.xiaomi.xmpush.server.Message;
import com.xiaomi.xmpush.server.Region;
import com.xiaomi.xmpush.server.Result;
import com.xiaomi.xmpush.server.Sender;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.UUID;

/**
 * @author linjw
 * @date 2019/11/5 11:03
 */
public class MiOverseasPush {
    private static String security = "";

    public static void main(String[] args) throws UnsupportedEncodingException {
        Map<String, Object> map = Maps.newHashMap();
        Map<String, String> content = Maps.newHashMap();
        content.put("data", "push");
        map.put("content", content);
        //String str = "intent:#Intent;launchFlags=0x10000000;package=com.seebaby;component=com.seebaby/com.szy.szypush.getui.GetuiTransferActivity;S.content=" + URLEncoder.encode(JSON.toJSONString(content), "UTF-8") + ";end";

        Sender sender = new Sender(security, Region.Other);
        Message message = new Message.Builder()
                .title("title")
                .description("description")
                .payload(JSON.toJSONString(content))
                .restrictedPackageName("com.yuanding.seebaby")
                .passThrough(0)
                .notifyType(1)
                .notifyId(UUID.randomUUID().hashCode())
                .timeToLive(360000)
                .extra("flow_control", "4000")
                .extra(Constants.EXTRA_PARAM_NOTIFY_FOREGROUND, "1")
                //.extra("callback", "https://oa.szy.cn:15280/push/mi/callback")
                //.extra("callback.param", "teacher")
                .build();

        Result result = null;
        try {
            result = sender.send(message, "", 0);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        System.out.println(JSON.toJSONString(result));
    }
}

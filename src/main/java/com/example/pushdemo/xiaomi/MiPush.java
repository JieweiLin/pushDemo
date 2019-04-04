package com.example.pushdemo.xiaomi;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import com.xiaomi.xmpush.server.Constants;
import com.xiaomi.xmpush.server.Message;
import com.xiaomi.xmpush.server.Result;
import com.xiaomi.xmpush.server.Sender;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;
import java.util.UUID;

/**
 * @author 林杰炜 linjw
 * @Title TODO 类描述
 * @Description TODO 详细描述
 * @Copyright 2014-现在 厦门神州鹰掌通家园项目组
 * @date 2018/12/20 16:07
 */
public class MiPush {

    private static String security = "+GfetTDy+om1wrK/M/+FYA==";

    public static void main(String[] args) throws UnsupportedEncodingException {
        //.extra("callback", "https://oa.szy.cn:15280/push/mi/callback")
        //.extra("callback.param", "teacher")

        Map<String, Object> map = Maps.newHashMap();
        Map<String, String> content = Maps.newHashMap();
        content.put("data", "push");
        map.put("content", content);
        String str = "intent:#Intent;launchFlags=0x10000000;package=com.seebaby;component=com.seebaby/com.szy.szypush.getui.GetuiTransferActivity;S.content=" + URLEncoder.encode(JSON.toJSONString(content), "UTF-8") + ";end";

        Sender sender = new Sender(security);
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
                .build();

        Result result = null;
        try {
            result = sender.send(message, "VhjYc1RNVp7QaePs4vPPj7hQRIJGWZMIcY2YAlo4xSVnrHdbxqmXMBIXToy3v3y3", 0);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        System.out.println(JSON.toJSONString(result));
    }
}

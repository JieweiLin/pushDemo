package com.example.pushdemo.meizu;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.meizu.push.sdk.constant.ExtraParam;
import com.meizu.push.sdk.server.IFlymePush;
import com.meizu.push.sdk.server.constant.ResultPack;
import com.meizu.push.sdk.server.model.push.PushResult;
import com.meizu.push.sdk.server.model.push.VarnishedMessage;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

/**
 * @author 林杰炜 linjw
 * @date 2018/12/20 15:53
 */
public class MeiZuPush {

    private static String appSecret = "";
    private static Long appId = 0L;

    public static void main(String[] args) throws UnsupportedEncodingException {
        IFlymePush push = new IFlymePush(appSecret);
        //.customAttribute("{\"content\":\"{'key1':'value1','key2':'value2'}\"}")
        Map<String, String> content = Maps.newHashMap();
        content.put("data", "push");
        String str = "intent:#Intent;launchFlags=0x10000000;package=com.seebaby;component=com.seebaby/com.szy.szypush.getui.GetuiTransferActivity;S.content=" + URLEncoder.encode(JSON.toJSONString(content), "UTF-8") + ";end";
        VarnishedMessage message = new VarnishedMessage.Builder()
                .appId(appId)
                .title("title")
                .content("content")
                .clickType(3)
                .customAttribute(JSON.toJSONString(content))
                //.extra(ExtraParam.CALLBACK.getKey(), "https://oa.szy.cn:15280/push/meizu/callback")
                .validTime(1)
                .build();
        List<String> pushIds = Lists.newArrayList();
        pushIds.add("");
        ResultPack<PushResult> result = null;
        try {
            result = push.pushMessage(message, pushIds, 0);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (result != null){
            System.out.println(JSON.toJSONString(result));
            System.out.println(JSON.toJSONString(result.value()));
        }
    }
}

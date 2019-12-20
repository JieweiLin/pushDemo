package com.example.pushdemo.vivo;

import com.vivo.push.sdk.notofication.Message;
import com.vivo.push.sdk.notofication.Result;
import com.vivo.push.sdk.server.Sender;

/**
 * @author linjw
 * @date 2019/12/11 19:19
 */
public class VivoPushV2 {
    private static Integer appId = 0;
    private static String appKey = "";
    private static String appSecret = "";

    public static void main(String[] args) throws Exception {
        Sender sender = new Sender(appSecret);
        Result result = sender.getToken(appId, appKey);
        sender.setAuthToken(result.getAuthToken());
        Message message = new Message.Builder()
                .regId("")
                .notifyType(1)
                .title("title")
                .content("content")
                .skipType(3)
                .skipContent("")
                .networkType(-1)
                .timeToLive(3600 * 24)
                .requestId("")
                .extra("https://oa.szy.cn:15280/push/vivo/callback", "callbackParam")
                .build();
        Result result1 = sender.sendSingle(message);

    }
}

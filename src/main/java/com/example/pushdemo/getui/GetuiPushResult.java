package com.example.pushdemo.getui;

import com.alibaba.fastjson.JSON;
import com.gexin.rp.sdk.http.IGtPush;

import java.util.Map;

/**
 * @author 林杰炜 linjw
 * @date 2018/12/26 16:46
 */
public class GetuiPushResult {

    private static final String mastersecret = "Ml3xwhmLlBAzPNa33odPu2";
    private static final String appkey = "6wvPDgXG8h5MIzqzWxHgWA";
    private static final String taskid = "OSAPNS-1227_iMKPgpzwTX8Xc5ByKqfdn4";
    static String host = "http://sdk.open.api.igexin.com/apiex.htm";

    public static void main(String[] args) {
        IGtPush push = new IGtPush(host, appkey, mastersecret);
        Map<String, Object> result = push.getPushResult(taskid).getResponse();
        System.out.println(JSON.toJSONString(result));
    }
}

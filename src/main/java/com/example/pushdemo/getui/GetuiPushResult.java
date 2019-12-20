package com.example.pushdemo.getui;

import com.alibaba.fastjson.JSON;
import com.gexin.rp.sdk.http.IGtPush;

import java.util.Map;

/**
 * @author 林杰炜 linjw
 * @date 2018/12/26 16:46
 */
public class GetuiPushResult {

    private static final String mastersecret = "";
    private static final String appkey = "";
    private static final String taskid = "";
    static String host = "http://sdk.open.api.igexin.com/apiex.htm";

    public static void main(String[] args) {
        IGtPush push = new IGtPush(host, appkey, mastersecret);
        Map<String, Object> result = push.getPushResult(taskid).getResponse();
        System.out.println(JSON.toJSONString(result));
    }
}

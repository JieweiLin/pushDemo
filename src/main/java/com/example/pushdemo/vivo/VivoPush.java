package com.example.pushdemo.vivo;

import com.alibaba.fastjson.JSON;
import com.gexin.rp.sdk.base.uitls.MD5Util;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.UUID;

/**
 * @author 林杰炜 linjw
 * @date 2018/12/20 13:41
 */
public class VivoPush {

    private static String appId = "";
    private static String appKey = "";
    private static String appSecret = "";
    private static final String host = "https://api-push.vivo.com.cn";
    private static String authToken = "";

    public static void main(String[] args) {
        if (StringUtils.isBlank(authToken)) {
            getAuthToken();
            System.out.println("获取成功！authToken:" + authToken);
        }

        String result = messageSend();
        if (StringUtils.isNotBlank(result)) {
            try {
                Map<String, Object> map1 = (Map<String, Object>) JSON.parseObject(result, Map.class);
                int resultCode = (int) map1.get("result");
                if (resultCode == 0) {
                    System.out.println("推送成功!, result:" + result);
                } else if (resultCode == 10070) {
                    System.out.println("推送超出限制!");
                } else {
                    System.out.println("推送失败!");
                }
            } catch (Exception e) {
                System.out.println("尴尬");
            }
        }
    }

    private static String messageSend() {
        String url = host.concat("/message/send");

        Map<String, Object> body = Maps.newHashMap();
        body.put("regId", "");
        body.put("notifyType", 1);
        body.put("title", "通知title");
        body.put("content", "通知content你好");
        body.put("skipType", 3);
        //body.put("skipContent", "");
        body.put("networkType", -1);

        Map<String, Object> content = Maps.newHashMap();
        content.put("key1", "value1");
        content.put("key2", "value2");
        body.put("skipContent", JSON.toJSONString(content));

        body.put("requestId", UUID.randomUUID().toString());

        Map<String, String> extra = Maps.newHashMap();
        extra.put("callback", "https://oa.szy.cn:15280/push/vivo/callback");
        //extra.put("callback.param", "token");
        body.put("extra", extra);

        String result = null;
        try {
            result = send(url, JSON.toJSONString(body), authToken);
        } catch (ConnectTimeoutException e) {
        }
        return result;
    }

    public static String getAuthToken() {
        String url = host.concat("/message/auth");
        long timestamp = System.currentTimeMillis();
        String sign = MD5Util.getMD5Format(appId + appKey + timestamp + appSecret).toLowerCase();

        Map<String, Object> body = Maps.newHashMap();
        body.put("appId", appId);
        body.put("appKey", appKey);
        body.put("timestamp", timestamp);
        body.put("sign", sign);

        String result = null;
        try {
            result = send(url, JSON.toJSONString(body), authToken);
        } catch (ConnectTimeoutException e) {
        }
        if (StringUtils.isNotBlank(result)) {
            Map<String, Object> map = (Map<String, Object>) JSON.parseObject(result, Map.class);
            int resultCode = (int) map.get("result");
            if (resultCode == 0) {
                authToken = String.valueOf(map.get("authToken"));
            } else {
                System.out.println("获取失败");
            }
        }
        return authToken;
    }

    public static String send(String url, String body, String authToken) throws ConnectTimeoutException {
        HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
        CloseableHttpClient closeableHttpClient = httpClientBuilder.build();
        String result = "";
        try {
            HttpPost post = new HttpPost(url);
            post.setHeader("Content-Type", "application/json");
            if (StringUtils.isNotBlank(authToken)) {
                post.setHeader("authToken", authToken);
            }
            RequestConfig config = RequestConfig.custom().setSocketTimeout(1000).setConnectTimeout(1000).setConnectionRequestTimeout(1000).setStaleConnectionCheckEnabled(true).build();
            post.setConfig(config);
            StringEntity stringEntity = new StringEntity(body, "utf-8");
            post.setEntity(stringEntity);
            HttpResponse response = closeableHttpClient.execute(post);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            StringBuffer sb = new StringBuffer();
            String line = "";
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line);
            }
            result = sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                closeableHttpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }
}

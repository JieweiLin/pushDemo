package com.example.pushdemo.umeng;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;

/**
 * @author 林杰炜 linjw
 * @Title TODO 类描述
 * @Description TODO 详细描述
 * @Copyright 2014-现在 厦门神州鹰掌通家园项目组
 * @date 2019/4/1 17:18
 */
public class UMengIOSPush {

    private static String appKey = "";
    private static String masterSecret = "";
    private static String deviceToken = "";

    public static void main(String[] args) {
        Map<String, Object> root = Maps.newHashMap();
        root.put("appkey", appKey);
        root.put("timestamp", Integer.toString((int) (System.currentTimeMillis() / 1000L)));
        root.put("type", "unicast");
        root.put("device_tokens", deviceToken);

        Map<String, Object> alert = Maps.newHashMap();
        alert.put("title", "title");
        alert.put("subtitle", "subtitle");
        alert.put("body", "body");

        Map<String, Object> aps = Maps.newHashMap();
        aps.put("alert", alert);
        aps.put("mutable-content", 1);
        aps.put("badge", 1);
        aps.put("sound", "default");
        aps.put("content-availabel", 0);

        Map<String, Object> payload = Maps.newHashMap();
        payload.put("aps", aps);
        Map<String, Object> customMsg = Maps.newHashMap();
        customMsg.put("groupid", "0B520D8F1B2A4FF3A0A2622E6CE84CAD");
        customMsg.put("message", "AAAA");
        payload.putAll(customMsg);

        root.put("payload", payload);

        Map<String, Object> policy = Maps.newHashMap();
        policy.put("start_time", "");
        policy.put("expire_time", "");
        policy.put("out_biz_no", "");
        policy.put("apns_collapse_id", "");
        root.put("policy", policy);

        root.put("production_mode", true);
        root.put("description", "");


    }

    public String send(Map<String, Object> root) throws Exception {
        HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
        CloseableHttpClient closeableHttpClient = httpClientBuilder.build();
        try {
            String url = "http://msg.umeng.com/api/send";
            String postBody = JSON.toJSONString(root);
            String sign = DigestUtils.md5Hex(("POST" + url + postBody + masterSecret).getBytes("utf8"));
            url = url + "?sign=" + sign;
            HttpPost post = new HttpPost(url);
            post.setHeader("User-Agent", "Mozilla/5.0");
            RequestConfig config = RequestConfig.custom().setSocketTimeout(1000).setConnectTimeout(1000).setConnectionRequestTimeout(1000).setStaleConnectionCheckEnabled(true).build();
            post.setConfig(config);
            StringEntity se = new StringEntity(postBody, "UTF-8");
            post.setEntity(se);
            HttpResponse response = closeableHttpClient.execute(post);
            BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            StringBuffer result = new StringBuffer();
            String line = "";
            while ((line = rd.readLine()) != null) {
                result.append(line);
            }

            return result.toString();
        } finally {
            try {
                closeableHttpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

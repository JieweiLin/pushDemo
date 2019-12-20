package com.example.pushdemo.umeng;

import com.alibaba.fastjson.JSON;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import push.AndroidNotification;
import push.AndroidUnicast;
import push.PushClient;
import push.UmengNotification;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;

/**
 * @author 林杰炜 linjw
 * @date 2018/12/21 14:40
 */
public class UMengPush extends PushClient {

    private static String appKey = "";
    private static String masterSecret = "";

    public static void main(String[] args) throws Exception {
        UMengPush uMengPush = new UMengPush();
        AndroidUnicast unicast = new AndroidUnicast(appKey, masterSecret);
        unicast.setDeviceToken("");
        unicast.setTicker("Android");
        unicast.setTitle("title");
        unicast.setDisplayType(AndroidNotification.DisplayType.MESSAGE);
        unicast.goAppAfterOpen();
        unicast.setCustomField("{\"groupid\":\"0B520D8F1B2A4FF3A0A2622E6CE84CAD\",\"message\":\"爸爸林杰炜于08点23分入校，请关注林杰炜是否已入校！\",\"msgid\":\"828742B035B53E668CD6FCA296D7DE76\",\"msgtext\":{\" childid\":\"1f7a9485eb11e632ac2d\",\"createTime\":\"2018-12-21 08:21:42\",\"msgcode\":\"m107\",\"schoolid\":\"GByyPPIpBxWgf5jhley\"},\"pushtype\":\"1\"}");
        unicast.setProductionMode();
        unicast.setMiActivity("com.szy.lib.push.MiPushActivity");

        String result = uMengPush.send(unicast);
        System.out.println(result);
    }

    @Override
    public String send(UmengNotification msg) throws Exception {
        HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
        CloseableHttpClient closeableHttpClient = httpClientBuilder.build();
        try {
            String timestamp = Integer.toString((int) (System.currentTimeMillis() / 1000L));
            msg.setPredefinedKeyValue("timestamp", timestamp);
            String url = "http://msg.umeng.com/api/send";
            String postBody = msg.getPostBody();
            Map<String, Object> map = JSON.parseObject(postBody, Map.class);
            map.put("receipt_url", "https://oa.szy.cn:15280/callback/push/umeng");
            map.put("receipt_type", 3);
            map.put("thirdparty_id", "Parent、828742B035B53E668CD6FCA296D7DE76");
            postBody = JSON.toJSONString(map);
            System.out.println(postBody);
            String sign = DigestUtils.md5Hex(("POST" + url + postBody + masterSecret).getBytes("utf8"));
            url = url + "?sign=" + sign;
            HttpPost post = new HttpPost(url);
            post.setHeader("User-Agent", "Mozilla/5.0");
            //设置请求超时时间
            RequestConfig config = RequestConfig.custom().setSocketTimeout(1000).setConnectTimeout(1000).setConnectionRequestTimeout(1000).setStaleConnectionCheckEnabled(true).build();
            post.setConfig(config);
            StringEntity se = new StringEntity(postBody, "UTF-8");
            post.setEntity(se);

            HttpResponse response = closeableHttpClient.execute(post);
            //int status = response.getStatusLine().getStatusCode();
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

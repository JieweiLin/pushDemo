package com.example.pushdemo.handler;

import com.alibaba.fastjson.JSON;
import com.example.pushdemo.cache.CacheHandler;
import com.example.pushdemo.common.Constant;
import com.example.pushdemo.model.AppInfo;
import com.example.pushdemo.model.DeviceInfo;
import com.example.pushdemo.model.PushConfig;
import com.example.pushdemo.model.PushInfo;
import com.example.pushdemo.model.PushRecord;
import com.example.pushdemo.model.PushResult;
import com.example.pushdemo.model.SingleMessage;
import com.example.pushdemo.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import push.AndroidNotification;
import push.AndroidUnicast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.Objects;

/**
 * @author 林杰炜 Linjw
 * @Title 友盟推送实现
 * @date 2020/9/28 8:39
 */
@Slf4j
public class UMengPushHandler extends BasePushHandler {

    private volatile static UMengPushHandler uMengPushHandler;
    public final static short U_MENG_PUSH_CHANNEL = Constant.PUSH_CHANNEL_UMENG;
    private final static String U_MENG_URL = "http://msg.umeng.com/api/send";

    private UMengPushHandler() {
    }

    public static UMengPushHandler getInstance() {
        if (Objects.isNull(uMengPushHandler)) {
            synchronized (UMengPushHandler.class) {
                if (Objects.isNull(uMengPushHandler)) {
                    uMengPushHandler = new UMengPushHandler();
                }
            }
        }
        return uMengPushHandler;
    }

    @Override
    PushResult handler(String no, SingleMessage singleMessage, PushRecord pushRecord, int passThrough, AppInfo appInfo, PushConfig pushConfig, String pushToken) {
        try {
            AndroidUnicast unicast = new AndroidUnicast(pushConfig.getServiceAppKey(), pushConfig.getServiceMasterSecret());
            unicast.setDeviceToken(pushToken);
            //通知栏提示文字
            unicast.setTicker("");
            unicast.setTitle(singleMessage.getTitle());
            unicast.setText((StringUtils.isNotBlank(singleMessage.getTitle()) ? singleMessage.getTitle() : "") + singleMessage.getAlertMsg());
            unicast.setDisplayType(AndroidNotification.DisplayType.MESSAGE);
            unicast.goAppAfterOpen();
            unicast.setCustomField(JsonUtils.getJson(singleMessage.getTransmissionContent()));
            unicast.setProductionMode();
            unicast.setMiActivity("");
            unicast.setExpireTime("");

            String result = send(unicast, pushConfig);
            Map<String, Object> resultMap = JsonUtils.readValue(result, Map.class);
            String ret = (String) resultMap.get("ret");
            Map<String, String> data = (Map<String, String>) resultMap.get("data");
            if ("SUCCESS".equals(ret)) {
                return PushResult.builder().success(true).messageId(singleMessage.getMsgId()).reason(result).build();
            } else {
                return PushResult.builder().success(false).reason(result).build();
            }
        } catch (Exception e) {
            return PushResult.builder().success(false).reason("服务器响应异常").build();
        }
    }

    private String send(AndroidUnicast unicast, PushConfig pushConfig) throws Exception {
        HttpClientBuilder httpClient = HttpClientBuilder.create();
        CloseableHttpClient closeableHttpClient = httpClient.build();
        try {
            String timestamp = Integer.toString((int) (System.currentTimeMillis() / 1000L));
            unicast.setPredefinedKeyValue("timestamp", timestamp);
            String postBody = unicast.getPostBody();
            Map<String, Object> map = JSON.parseObject(postBody, Map.class);
            map.put("receipt_url", "");
            map.put("receipt_type", 3);
            map.put("thirdparty_id", "");
            postBody = JSON.toJSONString(map);
            String sign = DigestUtils.md5Hex(("POST" + U_MENG_URL + postBody + pushConfig.getServiceMasterSecret()).getBytes(Charset.forName("utf8")));
            String url = U_MENG_URL + "?sign=" + sign;
            HttpPost post = new HttpPost(url);
            post.setHeader("User-Agent", "Mozilla/5.0");
            post.setConfig(RequestConfig.custom().setSocketTimeout(1000).setConnectTimeout(1000).setConnectionRequestTimeout(1000).setStaleConnectionCheckEnabled(true).build());
            StringEntity entity = new StringEntity(postBody, Charset.forName("UTF-8"));
            post.setEntity(entity);
            HttpResponse response = closeableHttpClient.execute(post);
            BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            StringBuffer sb = new StringBuffer();
            String line = "";
            while ((line = rd.readLine()) != null) {
                sb.append(line);
            }
            return sb.toString();
        } finally {
            try {
                closeableHttpClient.close();
            } catch (IOException e) {
            }
        }
    }

    @Override
    PushInfo getPushInfo(DeviceInfo deviceInfo) {
        log.info("uMengPushInfo: {}", U_MENG_PUSH_CHANNEL);
        return CacheHandler.getPushInfo(deviceInfo, U_MENG_PUSH_CHANNEL);
    }
}

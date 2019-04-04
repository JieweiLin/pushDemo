package com.example.pushdemo.hms;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import javafx.scene.input.DataFormat;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * @author 林杰炜 linjw
 * @Title TODO 类描述
 * @Description TODO 详细描述
 * @Copyright 2014-现在 厦门神州鹰掌通家园项目组
 * @date 2018/12/20 16:18
 */
public class HMSPush {
    private static String appSecret = "fseh086aclpp8eagdd4txcy0syn2z8ze";
    private static String appId = "10216510";
    private static String tokenUrl = "https://login.cloud.huawei.com/oauth2/v2/token";
    private static String apiUrl = "https://api.push.hicloud.com/pushsend.do";
    private static String accessToken = "CFw3+3uJVQ7Db8RU5Dvmd/ncCIXZru5Z0o/oTHtdhjJjwFiQ+QV9SFeCatiS1tX04FSeWEvTpWEuverkx6x49Q==";
    private static long tokenExpiredTime = 1547172731869L;

    public static void main(String[] args) {
        if (tokenExpiredTime <= System.currentTimeMillis()) {
            refreshToken();
        }
        try {
            JSONArray deviceTokens = new JSONArray();
            deviceTokens.add("0864678039742018200000352800CN01");

            JSONObject body = new JSONObject();
            body.put("title", "title");
            body.put("content", "content");

            JSONObject param = new JSONObject();
            param.put("intent", "intent:#Intent;launchFlags=0x10000000;package=com.seebaby;component=com.seebaby/com.szy.szypush.getui.GetuiTransferActivity;S.content=%7B%22data%22%3A%22push%22%7D;end");
            //param.put("intent", "shenzhouying://com.seebaby.push.huawei.transfer?content={\"data\":\"push\"}");

            JSONObject action = new JSONObject();
            action.put("type", 1);
            action.put("param", param);

            JSONObject msg = new JSONObject();
            msg.put("type", 3);
            msg.put("action", action);
            msg.put("body", body);

            JSONObject ext = new JSONObject();
            ext.put("biTag", "Parent、test");

            JSONObject hps = new JSONObject();
            hps.put("msg", msg);
            hps.put("ext", ext);

            JSONObject payload = new JSONObject();
            payload.put("hps", hps);

            DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
            Long time = System.currentTimeMillis() + 30*60*1000;
            String expireTime = df.format(time);

            String postBody = MessageFormat.format("access_token={0}&nsp_svc={1}&nsp_ts={2}&device_token_list={3}&payload={4}&expire_time={5}",
                    URLEncoder.encode(accessToken, "UTF-8"),
                    URLEncoder.encode("openpush.message.api.send", "UTF-8"),
                    URLEncoder.encode(String.valueOf(System.currentTimeMillis() / 1000), "UTF-8"),
                    URLEncoder.encode(deviceTokens.toString(), "UTF-8"),
                    URLEncoder.encode(payload.toString(), "UTF-8"),
                    URLEncoder.encode(expireTime, "UTF-8"));

            String postUrl = apiUrl
                    + "?nsp_ctx="
                    + URLEncoder.encode("{\"ver\":\"1\", \"appId\":\""
                            + appId + "\"}",
                    "UTF-8");

            String reuslt = httpPost(postUrl, postBody, 5000, 10000);

            System.out.println("result:" + reuslt);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

    }

    public static void refreshToken() {
        try {
            String msgBody = MessageFormat.format(
                    "grant_type=client_credentials&client_secret={0}&client_id={1}",
                    URLEncoder.encode(appSecret, "UTF-8"), appId);
            String response = httpPost(tokenUrl, msgBody, 5000, 5000);
            JSONObject obj = JSONObject.parseObject(response);
            accessToken = obj.getString("access_token");
            tokenExpiredTime = System.currentTimeMillis() + obj.getLong("expires_in") * 1000;
            System.out.println("accessToken:" + accessToken + " expiredTime: " + tokenExpiredTime);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    private static String httpPost(String httpUrl, String data, int connectTimeout, int readTimeout) {
        OutputStream outPut = null;
        HttpURLConnection urlConnection = null;
        InputStream in = null;

        try {
            URL url = new URL(httpUrl);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.setDoOutput(true);
            urlConnection.setDoInput(true);
            urlConnection.setRequestProperty("Content-Type",
                    "application/x-www-form-urlencoded; charset=UTF-8");
            urlConnection.setConnectTimeout(connectTimeout);
            urlConnection.setReadTimeout(readTimeout);
            urlConnection.connect();

            outPut = urlConnection.getOutputStream();
            outPut.write(data.getBytes("UTF-8"));
            outPut.flush();

            if (urlConnection.getResponseCode() < 400) {
                in = urlConnection.getInputStream();
            } else {
                in = urlConnection.getErrorStream();
            }

            List<String> lines = IOUtils.readLines(in,
                    urlConnection.getContentEncoding());
            StringBuffer stringBuffer = new StringBuffer();
            for (String line : lines) {
                stringBuffer.append(line);
            }
            return stringBuffer.toString();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(outPut);
            IOUtils.closeQuietly(in);
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
        return null;
    }
}

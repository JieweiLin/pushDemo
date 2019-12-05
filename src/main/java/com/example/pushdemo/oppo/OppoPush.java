package com.example.pushdemo.oppo;

import com.oppo.push.server.Notification;
import com.oppo.push.server.Result;
import com.oppo.push.server.Sender;
import com.oppo.push.server.Target;

import java.util.UUID;

/**
 * @author 林杰炜 linjw
 * @date 2018/12/20 13:36
 */
public class OppoPush {
    private static String appKey = "2nXqChrHn0u8Kso8kksO8g0CW";
    private static String masterSecret = "98dE9b3fb687ba611A468A9e26dA2f41";
    private static String authToken = "2727ab8f-4d96-4149-9d97-eb9b8debc41b";

    public static void main(String[] args) {
        try {
            Sender sender = new Sender(appKey, masterSecret);

            Notification notification = getNotification();
            Target target = Target.build("CN_d4926719a88fa4f774697fc228202c03");
            Result result = sender.unicastNotification(notification, target);
            System.out.println("statusCode:" + result.getStatusCode());
            System.out.println("returnCode:" + result.getReturnCode());
            System.out.println("messageId:" + result.getMessageId());
            System.out.println("result:" + result.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static Notification getNotification() {
        Notification notification = new Notification();
        /*通知栏title*/
        notification.setTitle("掌通家园");
        /*通知栏子title*/
        notification.setSubTitle("222");
        /*通知栏内容*/
        notification.setContent("oppoPushDemoContent9");

        /*自定义消息Id*/
        notification.setAppMessageId(UUID.randomUUID().toString());
        /*动作类型：0-启动应用 1-打开应用内页（activity的intent action）2-打开网页 4-打开应用内页（activity） 【非必填，默认值为0】 5-Intent scheme URL*/
        notification.setClickActionType(1);
        /*clickActionType为1或4时必填,长度500*/
        notification.setClickActionActivity("com.seebaby.push.oppo.transfer");
        notification.setActionParameters("{\"content\":\"{'key1':'value1','key2':'value2'}\"}");

        /*展示类型:0-即时,1-定时*/
        notification.setShowTimeType(0);
        /*是否进离线消息*/
        notification.setOffLine(true);
        /*离线消息存活时间（单位:秒）,最长3天*/
        notification.setOffLineTtl(24*3600);
        /*时区*/
        notification.setTimeZone("GMT+08:00");
        /*网络类型:0-不限联网方式,1-仅wifi推送*/
        notification.setNetworkType(0);

        /*回调URL*/
        //notification.setCallBackUrl("http://120.36.131.86:15280/push/oppo/callback");
        /*notification.setCallBackUrl("https://oa.szy.cn:15280/push/oppo/callback");
        notification.setCallBackParameter("Teacher");*/

        return notification;
    }
}

package com.example.pushdemo.fcm;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

import java.io.IOException;

/**
 * @author linjw
 * @date 2019/11/5 10:26
 */
public class GoogleFCMPush {

    public static void main(String[] args) throws IOException {

        /**
         * 应用默认凭据(ADC)能够隐式确定凭据
         * 需设置环境变量
         * Linux或macOS: export GOOGLE_APPLICATION_CREDENTIALS="/home/user/Downloads/service-account-file.json"
         * Windows: $env:GOOGLE_APPLICATION_CREDENTIALS="C:/Users/username/Downloads/service-account-file.json"
         */
        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.getApplicationDefault())
                .setDatabaseUrl("https://<DATABASE_NAME>.firebaseio.com/")
                .build();
        FirebaseApp.initializeApp(options);

        /**
         * 使用OAuth 2.0 刷新令牌
         */
        /*FileInputStream refreshToken = new FileInputStream("path/to/refreshToken.json");
        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(refreshToken))
                .setDatabaseUrl("https://<DATABASE_NAME>.firebaseio.com/")
                .build();
        FirebaseApp.initializeApp(options);*/
    }
}

package com.example.pushdemo.ons;

import com.aliyun.openservices.ons.api.Action;
import com.aliyun.openservices.ons.api.ConsumeContext;
import com.aliyun.openservices.ons.api.Message;
import com.aliyun.openservices.ons.api.MessageListener;
import com.example.pushdemo.utils.JsonUtils;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * @author 林杰炜 Linjw
 * @Title ons 消费监听
 * @date 2020/5/21 16:24
 */
@Log4j2
@Component
public class OnsConsumerListener implements MessageListener {

    private static ThreadLocal<String> threadLocal = new ThreadLocal<>();

    @Override
    public Action consume(Message message, ConsumeContext context) {
        log.info("接收到ons消息, message: {}", JsonUtils.getJson(message));
        int i = (int) (Math.random() * 10);
        String no = UUID.randomUUID().toString().trim().replaceAll("-", "").substring(i, i + 20);
        try {
            threadLocal.set(no);
            String messageBody = String.valueOf(message.getBody());

        } catch (Exception e) {

        }
        return null;
    }

    public static ThreadLocal<String> getThreadLocal() {
        return threadLocal;
    }
}

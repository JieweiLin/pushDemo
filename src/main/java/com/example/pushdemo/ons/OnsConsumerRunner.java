package com.example.pushdemo.ons;

import com.aliyun.openservices.ons.api.MessageListener;
import com.aliyun.openservices.ons.api.bean.ConsumerBean;
import com.aliyun.openservices.ons.api.bean.Subscription;
import com.google.common.collect.Maps;
import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

/**
 * @author 林杰炜 Linjw
 * @Title ons 消费方启动
 * @date 2020/5/21 16:17
 */
@Data
@Log4j2
@Configuration
public class OnsConsumerRunner implements ApplicationRunner {

    @Autowired
    private OnsConsumerListener onsConsumerListener;

    @Autowired
    private ConsumerBean consumerBean;


    @Override
    public void run(ApplicationArguments args) throws Exception {
        initOns();
    }

    private void initOns() {
        Map<Subscription, MessageListener> map = Maps.newHashMap();

        Subscription subscription = new Subscription();
        subscription.setTopic("");
        subscription.setExpression("");
        map.put(subscription, onsConsumerListener);

        consumerBean.setSubscriptionTable(map);
        log.info("启动ons消费...");
        consumerBean.start();
        log.info("启动ons消费成功...");
    }
}

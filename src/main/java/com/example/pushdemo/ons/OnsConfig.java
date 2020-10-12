package com.example.pushdemo.ons;

import com.aliyun.openservices.ons.api.PropertyKeyConst;
import com.aliyun.openservices.ons.api.bean.ConsumerBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

/**
 * @author 林杰炜 Linjw
 * @Title TODO 类描述
 * @date 2020/5/21 16:26
 */
@Configuration
public class OnsConfig {

    @Bean(destroyMethod = "shutdown")
    public ConsumerBean consumerBean() {
        Properties properties = new Properties();
        properties.setProperty(PropertyKeyConst.GROUP_ID, "");
        properties.setProperty(PropertyKeyConst.AccessKey, "");
        properties.setProperty(PropertyKeyConst.SecretKey, "");
        ConsumerBean consumer = new ConsumerBean();
        consumer.setProperties(properties);
        return consumer;
    }
}

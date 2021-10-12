package com.bitcom.config;

import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class ProducerConfiguration {
    @Value("${epark.rocketmq.nameserver}")
    private String nameServer;

    @Bean(destroyMethod = "shutdown")
    public DefaultMQProducer defaultMQProducer() throws Exception {
        DefaultMQProducer producer = new DefaultMQProducer("p-epark-pay-service");

        producer.setNamesrvAddr(this.nameServer);

        try {
            producer.start();
        } catch (MQClientException e) {
            e.printStackTrace();
            throw new Exception("RocketMQ Producer init failure.", (Throwable) e);
        }
        return producer;
    }
}




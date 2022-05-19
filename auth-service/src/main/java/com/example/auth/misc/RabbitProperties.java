package com.example.auth.misc;

import com.example.shared.rabbithole.QueueData;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "rabbit.users")
public class RabbitProperties {

    private String exchangeName;

    private final QueueData verify = new QueueData();

    public String getExchangeName() {
        return exchangeName;
    }

    public QueueData getVerify() {
        return verify;
    }

    public void setExchangeName(String exchangeName) {
        this.exchangeName = exchangeName;
    }
}

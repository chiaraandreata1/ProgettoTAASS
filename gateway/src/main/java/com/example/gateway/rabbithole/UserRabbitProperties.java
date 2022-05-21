package com.example.gateway.rabbithole;

import com.example.shared.rabbithole.QueueData;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "rabbit.users")
public class UserRabbitProperties {

    private String exchangeName;

    private final QueueData auth = new QueueData();

    public QueueData getAuth() {
        return auth;
    }

    public String getExchangeName() {
        return exchangeName;
    }

    public void setExchangeName(String exchangeName) {
        this.exchangeName = exchangeName;
    }
}

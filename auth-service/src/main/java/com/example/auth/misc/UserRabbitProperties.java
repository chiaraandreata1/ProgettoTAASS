package com.example.auth.misc;

import com.example.shared.rabbithole.QueueData;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "rabbit.users")
public class UserRabbitProperties {

    private String exchangeName;

    private final QueueData auth = new QueueData();
    private final QueueData getInfo = new QueueData();
    private final QueueData verify = new QueueData();

    public QueueData getAuth() {
        return auth;
    }

    public QueueData getGetInfo() {
        return getInfo;
    }

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

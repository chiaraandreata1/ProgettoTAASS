package com.example.tournamentservice.rabbithole;

import com.example.shared.rabbithole.QueueData;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "rabbit.users")
public class UserRabbitProperties {

    private String exchangeName;

    private final QueueData getInfo = new QueueData();

    public String getExchangeName() {
        return exchangeName;
    }

    public QueueData getGetInfo() {
        return getInfo;
    }

    public void setExchangeName(String exchangeName) {
        this.exchangeName = exchangeName;
    }
}


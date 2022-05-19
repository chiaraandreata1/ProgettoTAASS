package com.example.tournamentservice.rabbithole;

import com.example.shared.rabbithole.QueueData;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "rabbit.facility")
public class FacilityRabbitProperties {

    private String exchangeName;

    private final QueueData hours = new QueueData();

    private final QueueData sports = new QueueData();

    public String getExchangeName() {
        return exchangeName;
    }

    public QueueData getHours() {
        return hours;
    }

    public QueueData getSports() {
        return sports;
    }

    public void setExchangeName(String exchangeName) {
        this.exchangeName = exchangeName;
    }
}


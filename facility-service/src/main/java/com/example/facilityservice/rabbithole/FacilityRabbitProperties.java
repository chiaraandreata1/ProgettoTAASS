package com.example.facilityservice.rabbithole;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "rabbit.facility")
public class FacilityRabbitProperties {

    private String exchangeName;

    private final QueueData hours = new QueueData();

    public String getExchangeName() {
        return exchangeName;
    }

    public QueueData getHours() {
        return hours;
    }

    public void setExchangeName(String exchangeName) {
        this.exchangeName = exchangeName;
    }
}

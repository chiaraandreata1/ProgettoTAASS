package com.example.reservationservice.rabbithole;

import com.example.shared.models.QueueData;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "rabbit.reservation")
public class ReservationRabbitProperties {

    private String exchangeName;

    private final QueueData reserve = new QueueData();

    public String getExchangeName() {
        return exchangeName;
    }

    public QueueData getReserve() {
        return reserve;
    }

    public void setExchangeName(String exchangeName) {
        this.exchangeName = exchangeName;
    }
}

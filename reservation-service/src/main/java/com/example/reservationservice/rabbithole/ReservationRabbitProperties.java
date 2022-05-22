package com.example.reservationservice.rabbithole;

import com.example.shared.rabbithole.QueueData;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "rabbit.reservation")
public class ReservationRabbitProperties {

    private String exchangeName;

    private final QueueData delete = new QueueData();
    private final QueueData reserve = new QueueData();

    public String getExchangeName() {
        return exchangeName;
    }

    public QueueData getDelete() {
        return delete;
    }

    public QueueData getReserve() {
        return reserve;
    }

    public void setExchangeName(String exchangeName) {
        this.exchangeName = exchangeName;
    }
}

package com.example.reservationservice.rabbithole;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ReservationRabbitConfig {

    @Autowired
    private ReservationRabbitProperties properties;

    @Bean
    public DirectExchange directExchange() {
        return new DirectExchange(properties.getExchangeName());
    }

    @Bean
    public Queue reserveQueue() {
        return new Queue(properties.getReserve().getQueueName());
    }

    @Bean
    public Queue deleteQueue() {
        return new Queue(properties.getDelete().getQueueName());
    }

    @Bean
    public Declarables reserveBinding(@Autowired DirectExchange directExchange, @Autowired Queue deleteQueue, @Autowired Queue reserveQueue) {
        return new Declarables(
                BindingBuilder
                        .bind(deleteQueue)
                        .to(directExchange)
                        .with(properties.getDelete().getKey()),
                BindingBuilder
                        .bind(reserveQueue)
                        .to(directExchange)
                        .with(properties.getReserve().getKey())
        );
    }
}

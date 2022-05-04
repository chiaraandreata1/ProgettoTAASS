package com.example.reservationservice.rabbithole;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
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
    public Binding reserveBinding(@Autowired Queue reserveQueue, @Autowired DirectExchange directExchange) {
        return BindingBuilder
                .bind(reserveQueue)
                .to(directExchange)
                .with(properties.getReserve().getKey());
    }
}

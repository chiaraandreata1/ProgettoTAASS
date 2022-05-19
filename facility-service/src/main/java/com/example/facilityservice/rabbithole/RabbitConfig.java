package com.example.facilityservice.rabbithole;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    @Autowired
    private FacilityRabbitProperties properties;

    @Bean
    public Queue hoursQueue() {
        return new Queue(properties.getHours().getQueueName());
    }

    @Bean
    public Queue sportsQueue() {
        return new Queue(properties.getSports().getQueueName());
    }

    @Bean
    public DirectExchange rabbitDirectExchange() {
        return new DirectExchange(properties.getExchangeName());
    }

    @Bean
    public Declarables bindings(@Autowired DirectExchange directExchange,
                                @Autowired Queue hoursQueue,
                                @Autowired Queue sportsQueue) {
        return new Declarables(
                BindingBuilder
                .bind(hoursQueue)
                .to(directExchange)
                .with(properties.getHours().getKey()),

                BindingBuilder
                .bind(sportsQueue)
                .to(directExchange)
                .with(properties.getSports().getKey())
        );
    }

//    @Bean
//    public Binding hoursBinding(@Autowired Queue hoursQueue, @Autowired DirectExchange directExchange) {
//
//        return BindingBuilder
//                .bind(hoursQueue)
//                .to(directExchange)
//                .with(properties.getHours().getKey());
//    }

}

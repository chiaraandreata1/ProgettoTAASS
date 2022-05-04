package com.example.facilityservice.rabbithole;

import com.example.shared.models.FacilityHours;
import com.example.shared.models.RabbitRequest;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
public class RabbitConfig {

    @Autowired
    private FacilityRabbitProperties properties;

    @Bean
    public Queue hoursQueue() {
        return new Queue(properties.getHours().getQueueName());
    }

    @Bean
    public DirectExchange rabbitDirectExchange() {
        return new DirectExchange(properties.getExchangeName());
    }

    @Bean
    public Binding hoursBinding(@Autowired Queue hoursQueue, @Autowired DirectExchange directExchange) {

        return BindingBuilder
                .bind(hoursQueue)
                .to(directExchange)
                .with(properties.getHours().getKey());
    }
}

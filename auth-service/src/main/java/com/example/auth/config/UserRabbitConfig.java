package com.example.auth.config;

import com.example.auth.misc.RabbitProperties;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UserRabbitConfig {

    @Autowired
    private RabbitProperties properties;

    @Bean
    public Queue verifyQueue() {
        return new Queue(properties.getVerify().getQueueName());
    }

    @Bean
    public DirectExchange rabbitDirectExchange() {
        return new DirectExchange(properties.getExchangeName());
    }

    @Bean
    public Binding verifyBinding(@Autowired Queue verifyQueue, @Autowired DirectExchange directExchange) {
        return BindingBuilder.bind(verifyQueue)
                .to(directExchange)
                .with(properties.getVerify().getKey());
    }
}

package com.example.auth.config;

import com.example.auth.misc.UserRabbitProperties;
import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UserRabbitConfig {

    @Autowired
    private UserRabbitProperties properties;

    @Bean
    public Queue authQueue() {
        return new Queue(properties.getAuth().getQueueName());
    }

    @Bean
    public Queue getInfoQueue() {
        return new Queue(properties.getGetInfo().getQueueName());
    }

    @Bean
    public Queue verifyQueue() {
        return new Queue(properties.getVerify().getQueueName());
    }

    @Bean
    public DirectExchange rabbitDirectExchange() {
        return new DirectExchange(properties.getExchangeName());
    }

    @Bean
    public Declarables bindings(@Autowired DirectExchange directExchange,
                                @Autowired Queue authQueue,
                                @Autowired Queue getInfoQueue,
                                @Autowired Queue verifyQueue) {
        return new Declarables(
                BindingBuilder.bind(authQueue)
                        .to(directExchange)
                        .with(properties.getAuth().getKey()),
                BindingBuilder.bind(getInfoQueue)
                        .to(directExchange)
                        .with(properties.getGetInfo().getKey()),
                BindingBuilder.bind(verifyQueue)
                        .to(directExchange)
                        .with(properties.getVerify().getKey())
        );
    }

//    @Bean
//    public Binding verifyBinding(@Autowired Queue verifyQueue, @Autowired DirectExchange directExchange) {
//        return BindingBuilder.bind(verifyQueue)
//                .to(directExchange)
//                .with(properties.getVerify().getKey());
//    }
}

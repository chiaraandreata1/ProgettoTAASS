package com.example.facilityservice.rabbithole;

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

    public static class Client {

        @Autowired
        private RabbitTemplate rabbitTemplate;

        @Autowired
        private DirectExchange directExchange;

        @Autowired
        private FacilityRabbitProperties properties;

        public void ask() {

            Map<String, Integer> response = (Map<String, Integer>)
                    rabbitTemplate.convertSendAndReceive(directExchange.getName(), properties.getHours().getKey(), "");

            System.out.println("Got " + response + "");
        }

    }


    @Value("${queue.name}")
    private String queueName;
//
//    @Value("${xchange.name}")
//    private String directXchangeName;

//    @Bean
//    public Queue queue() {
//        return new Queue(queueName);
//    }
//
//    @Bean
//    public DirectExchange directExchange() {
//        return new DirectExchange(directXchangeName);
//    }

//    @Bean
//    public Binding binding(DirectExchange exchange, Queue queue) {
//        return BindingBuilder.bind(queue).to(exchange).with("roytuts");
//    }

    @Bean
    public Client client() {
        return new Client();
    }
}

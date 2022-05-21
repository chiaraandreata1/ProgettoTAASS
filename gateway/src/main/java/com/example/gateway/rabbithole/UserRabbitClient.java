package com.example.gateway.rabbithole;

import com.example.shared.rabbithole.RabbitTemplateWrapper;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;

@Component
public class UserRabbitClient {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private UserRabbitProperties properties;

    public UsernamePasswordAuthenticationToken validateToken(String token) {
        return new RabbitTemplateWrapper(rabbitTemplate)
                .send(properties.getExchangeName(), properties.getAuth(), token);
    }
}

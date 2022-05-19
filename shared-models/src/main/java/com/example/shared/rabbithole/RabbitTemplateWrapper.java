package com.example.shared.rabbithole;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.core.ParameterizedTypeReference;

public class RabbitTemplateWrapper {

    private final RabbitTemplate rabbitTemplate;

    public RabbitTemplateWrapper(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public <R, A> A send(String exchangeName, QueueData queueData, R requestBody) {
        return send(exchangeName, queueData, null, requestBody);
    }

    public <R, A> A send(String exchangeName, QueueData queueData, String message, R requestBody) {

        RabbitRequest<R> request;



        request = new RabbitRequest<>(message, requestBody);


        RabbitResponse<A> response = (RabbitResponse<A>) rabbitTemplate.convertSendAndReceive(exchangeName,
                queueData.getKey(),
                request);

        RabbitResponse.check(response);

        return response.getBody();
    }
}

package com.example.lessonservice.rabbithole;

import com.example.shared.models.FacilityHours;
import com.example.shared.models.facility.SportInfo;
import com.example.shared.rabbithole.RabbitResponse;
import com.example.shared.rabbithole.RabbitTemplateWrapper;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FacilityRabbitClient {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private FacilityRabbitProperties properties;

    public SportInfo getSportInfo(Long sportID) {
        return new RabbitTemplateWrapper(rabbitTemplate)
                .send(properties.getExchangeName(), properties.getSports(), sportID);
    }

    public FacilityHours getHours() {
        RabbitResponse<FacilityHours> response = (RabbitResponse<FacilityHours>)
                rabbitTemplate.convertSendAndReceive(properties.getExchangeName(), properties.getHours().getKey(), "");

        RabbitResponse.check(response);

        return response.getBody();
    }
}

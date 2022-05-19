package com.example.auth.controllers;

import com.example.auth.models.UserEntity;
import com.example.auth.repositories.UserEntityRepository;
import com.example.shared.rabbithole.RabbitResponse;
import com.example.shared.rabbithole.UserStatusRequest;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UserRabbitController {

    @Autowired
    private UserEntityRepository userEntityRepository;

    @RabbitListener(queues = "${rabbit.users.verify.queue-name}")
    public RabbitResponse<Boolean> verifyUser(UserStatusRequest userStatusRequest) {

        boolean res;
        Optional<UserEntity> userEntity;

        userEntity = userEntityRepository.findById(userStatusRequest.getId());

        res = userEntity.isPresent();

        if (res && userStatusRequest.getTypes() != null && !userStatusRequest.getTypes().isEmpty())
            res = userStatusRequest.getTypes().contains(userEntity.get().getType());

        return new RabbitResponse<>(res);
    }
}

package com.example.auth.controllers;

import com.example.auth.models.LocalUser;
import com.example.auth.models.UserEntity;
import com.example.auth.repositories.UserEntityRepository;
import com.example.auth.services.LocalUserDetailService;
import com.example.auth.services.TokenService;
import com.example.shared.models.users.UserInfo;
import com.example.shared.rabbithole.RabbitRequest;
import com.example.shared.rabbithole.RabbitResponse;
import com.example.shared.rabbithole.UserStatusRequest;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class UserRabbitController {

    @Autowired
    private TokenService tokenProvider;

    @Autowired
    private UserEntityRepository userEntityRepository;

    @Autowired
    private LocalUserDetailService customUserDetailsService;

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

    @RabbitListener(queues = "${rabbit.users.auth.queue-name}")
    public RabbitResponse<UsernamePasswordAuthenticationToken> authToken(RabbitRequest<String> request) {

        RabbitResponse<UsernamePasswordAuthenticationToken> response;

        try {
            String jwt = request.getRequestBody();

            if (StringUtils.hasText(jwt)) {
                tokenProvider.validateTokenThrow(jwt);
                Long userId = tokenProvider.getUserIdFromToken(jwt);

                UserDetails userDetails = customUserDetailsService.loadUserById(userId).toUserDetails();

                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities());

                authentication.setDetails("RabbitMQ");
                response = new RabbitResponse<>(authentication);
            } else
                response = new RabbitResponse<>(HttpStatus.INTERNAL_SERVER_ERROR, "");
        } catch (ResponseStatusException ex) {
            response = new RabbitResponse<>(ex.getStatus(), ex.getMessage());
        } catch (Exception ex) {
            response = new RabbitResponse<>(HttpStatus.BAD_REQUEST, ex.getMessage());
        }

        return response;
    }

    @RabbitListener(queues = "${rabbit.users.get-info.queue-name}")
    public RabbitResponse<List<UserInfo>> getInfo(RabbitRequest<List<Long>> request) {

        List<UserInfo> infoList = request.getRequestBody()
                .stream()
                .map(customUserDetailsService::loadUserById)
                .map(LocalUser::toUserDetails)
                .map(com.example.shared.models.users.UserDetails::toUserInfo)
                .collect(Collectors.toList());

        return new RabbitResponse<>(infoList);

    }
}

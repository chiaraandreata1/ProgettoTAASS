package com.example.auth.controllers;

import com.example.auth.config.CurrentUser;
import com.example.auth.models.LocalUser;
import com.example.auth.models.UserEntity;
import com.example.auth.repositories.UserEntityRepository;
import com.example.shared.models.users.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
//@RequestMapping("/api/user")
public class UserRestController {

    @Autowired
    private UserEntityRepository userEntityRepository;

    @GetMapping("/me")
    @PreAuthorize("hasRole('USER')")
    public UserInfo getCurrentUser(@CurrentUser LocalUser user) {
        return user.toUserInfo();
    }

    @GetMapping("find-users")
    public List<UserInfo> findUser(@CurrentUser LocalUser user,
                                   @RequestParam(name = "query") String partEmail,
                                   @RequestParam(required = false) List<String> excluded,
                                   @RequestParam(required = false, defaultValue = "5") int limit) {
        if (excluded == null)
            excluded = new ArrayList<>();
        else
            excluded = new ArrayList<>(excluded);

        if (user != null)
            excluded.add(user.getEmail());

        if (limit > 20)
            limit = 20;

        List<UserEntity> res;

        if (excluded.isEmpty())
            res = userEntityRepository
                    .findUserEntitiesByEmailContainingIgnoreCase(partEmail);
        else
            res = userEntityRepository
                    .findUserEntitiesByEmailContainingIgnoreCaseAndEmailNotIn(partEmail, excluded);

        return res
                .stream()
                .limit(limit)
                .map(UserEntity::toUserInfo)
                .collect(Collectors.toList());
    }

}

package com.example.auth.controllers;

import com.example.shared.models.users.UserDetails;
import com.example.shared.tools.CurrentUser;
import com.example.auth.models.LocalUser;
import com.example.auth.models.UserEntity;
import com.example.auth.repositories.UserEntityRepository;
import com.example.shared.models.users.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
//@RequestMapping("/api/user")
public class UserRestController {

    @Autowired
    private HttpSession httpSession;

    @Autowired
    private UserEntityRepository userEntityRepository;

    @GetMapping("/me")
    @PreAuthorize("hasRole('USER')")
    public UserInfo getCurrentUser(@CurrentUser UserDetails user, @RequestHeader Map<String, String> headers) {

        User userEx = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        httpSession.setAttribute("useName", user.getUsername());
        httpSession.setAttribute("authorities", user.getAuthorities());

//        userEntityRepository.findByEmail(user.getUsername()).toUserInfo();

        return user.toUserInfo();
    }

    @GetMapping("/session")
    public String getSession(@CurrentUser UserInfo localUser) {
        return String.format("%s %s\n", localUser.getEmail(),  RequestContextHolder.getRequestAttributes().getSessionId());
    }

    @GetMapping("/headers")
    public String getHeaders(@RequestHeader Map<String, String> headers) {
        return headers.toString().replaceAll(";", "<br>");
    }

    @GetMapping("find-users")
    public List<UserInfo> findUser(@CurrentUser UserInfo user,
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

    @GetMapping("fix-session")
    public Object fixSession() {
        return null;
    }

}

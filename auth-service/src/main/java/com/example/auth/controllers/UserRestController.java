package com.example.auth.controllers;

import com.example.shared.models.users.UserDetails;
import com.example.shared.models.users.UserType;
import com.example.shared.tools.CurrentUser;
import com.example.auth.models.LocalUser;
import com.example.auth.models.UserEntity;
import com.example.auth.repositories.UserEntityRepository;
import com.example.shared.models.users.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Comparator;
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

    @GetMapping("boss")
    @PreAuthorize("hasRole('ADMIN')")
    public String boss() {
        return "boss";
    }

    @GetMapping("get")
    public UserInfo getUser(@RequestParam Long id) {
        UserEntity userEntity = userEntityRepository
                .findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        return userEntity.toUserInfo();
    }

    @GetMapping("get-users")
    public List<UserInfo> getUsers(@RequestParam List<Long> ids) {
        return this.userEntityRepository.findAllById(ids).stream()
                .sorted(Comparator.comparingInt(o -> ids.indexOf(o.getId())))
                .map(UserEntity::toUserInfo)
                .collect(Collectors.toList());
    }

    //L'HO INSERITA IO PERCHÈ È NECESSARIA AL CORSO CHE CHIEDE I SUGGERIMENTI DI SOLI ISTRUTTORI.
    //SE VUOI PUOI MIGLIORARLA, MA È NECESSARIA. HO AGGIUNTO ANCHE IL FINDBYTYPE NEL REPOSITORY
    @GetMapping("get-instructors")
    //@PreAuthorize("hasRole('ADMIN')")
    public List<UserInfo> findInstructors() {
        List<UserEntity> userEntities = userEntityRepository.findAllByType(UserType.TEACHER);
        List<UserInfo> userInfos = new ArrayList<>();
        for (UserEntity userEntity : userEntities) {
            userInfos.add(userEntity.toUserInfo());
        }
        return userInfos;
    }

    @GetMapping("find-users")
    public List<UserInfo> findUser(@CurrentUser UserInfo user,
                                   @RequestParam(name = "query") String partEmail,
                                   @RequestParam(required = false) List<Long> excluded,
                                   @RequestParam(required = false, defaultValue = "5") int limit) {

        if (excluded == null)
            excluded = new ArrayList<>();
        else
            excluded = new ArrayList<>(excluded);

        if (user != null)
            excluded.add(user.getId());

        if (limit > 20)
            limit = 20;

        List<UserEntity> res;

        if (excluded.isEmpty())
            res = userEntityRepository
                    .findUserEntitiesByEmailContainingIgnoreCase(partEmail);
        else
            res = userEntityRepository
                    .findUserEntitiesByEmailContainingIgnoreCaseAndIdNotIn(partEmail, excluded);

        return res
                .stream()
                .limit(limit)
                .map(UserEntity::toUserInfo)
                .collect(Collectors.toList());
    }

    @GetMapping("find-players")
    public List<UserInfo> findPlayers(@CurrentUser UserInfo user,
                                      @RequestParam(name = "query") String partEmail,
                                      @RequestParam(required = false) List<Long> excluded,
                                      @RequestParam(required = false, defaultValue = "5") int limit) {

        if (excluded == null)
            excluded = new ArrayList<>();
        else
            excluded = new ArrayList<>(excluded);

        if (user != null)
            excluded.add(user.getId());

        if (limit > 20)
            limit = 20;

        List<UserEntity> res;

        if (excluded.isEmpty())
            res = userEntityRepository
                    .findUserEntitiesByEmailContainingIgnoreCaseAndTypeIs(partEmail, UserType.PLAYER);
        else
            res = userEntityRepository.findUserEntitiesByEmailContainingIgnoreCaseAndTypeIsAndIdNotIn(partEmail,
                    UserType.PLAYER,
                    excluded);

        return res
                .stream()
                .limit(limit)
                .map(UserEntity::toUserInfo)
                .collect(Collectors.toList());
    }

    @GetMapping("log-out")
    public void logout() {
        SecurityContextHolder.clearContext();
    }

}

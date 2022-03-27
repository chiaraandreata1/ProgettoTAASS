package com.example.userservice.controllers;

import com.example.userservice.models.User;
import com.example.userservice.models.UserInfo;
import com.example.userservice.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class UserController {

    /*
    Example UserInfos
     */
    static List<UserInfo> userInfos = new ArrayList<>();
    static {
        String[] names = new String[]{"alfio", "betta", "claudio", "dina", "elio", "federica", "gaio", "hensel"};
        for (int i = 0; i < names.length; i++) {
            UserInfo userInfo = new UserInfo();
            userInfo.setId((long) i);
            userInfo.setShownName(names[i]);
            userInfo.setUserName(names[i].substring(0, 1).toUpperCase() + names[i].substring(1));
            userInfo.setType(UserInfo.Type.PLAYER);
            userInfos.add(userInfo);
        }
    }

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/")
    public List<User> listAllUsers(){
        return userRepository.findAll();
    }

    @GetMapping("/typeuser/{typeuser}")
    public List<User> listUsersByType(@PathVariable String typeuser){ return userRepository.findAllByTypeuser(typeuser); }

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.OK)
    public void create(@RequestBody User user){
        userRepository.save(user);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUsers(@PathVariable("id") long id){
        System.out.println("Delete user with id = " + id + "...");

        userRepository.deleteById(id);

        return new ResponseEntity<>("User deleted!", HttpStatus.OK);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteAllUsers(){
        System.out.println("Delete all users...");

        userRepository.deleteAll();

        return new ResponseEntity<>("All users have been deleted!", HttpStatus.OK);
    }

    @GetMapping("suggestions")
    public List<UserInfo> getSuggested(@RequestParam() String input, @RequestParam(required = false, defaultValue = "5") int limit) {
        if (limit > 20)
            limit = 20;

        return userInfos.stream()
                .filter(userInfo -> userInfo.getUserName().toLowerCase(Locale.ROOT)
                        .contains(input.toLowerCase(Locale.ROOT)))
                .limit(limit)
                .collect(Collectors.toList());
    }

    @GetMapping("find-user-info")
    public UserInfo findUserInfo(@RequestParam() String username) {
        Optional<UserInfo> first = userInfos.stream().filter(userInfo -> Objects.equals(userInfo.getUserName(), username)).findFirst();
        return first.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
    }

    @RequestMapping(value = "user-info", method = RequestMethod.GET, params = {"id"})
    public UserInfo getUserInfo(@RequestParam() Long id) {
        Optional<UserInfo> first = userInfos.stream().filter(userInfo -> Objects.equals(userInfo.getId(), id)).findFirst();
        return first.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
    }

    @RequestMapping(value = "user-info", method = RequestMethod.GET, params = {"ids"})
    public List<UserInfo> getUserInfos(@RequestParam() List<Long> ids) {
        return userInfos.stream().filter(userInfo -> ids.contains(userInfo.getId()))
                .collect(Collectors.toList());
    }
}

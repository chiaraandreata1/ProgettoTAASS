package com.example.auth.controllers;

import com.example.auth.config.CurrentUser;
import com.example.auth.models.LocalUser;
import com.example.auth.models.UserInfo;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
//@RequestMapping("/api/user")
public class UserController {

    @GetMapping("/me")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<UserInfo> getCurrentUser(@CurrentUser LocalUser user) {
        return ResponseEntity.ok(user.toUserInfo());
    }
}

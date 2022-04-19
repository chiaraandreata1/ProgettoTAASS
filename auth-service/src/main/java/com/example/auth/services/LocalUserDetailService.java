package com.example.auth.services;

import com.example.auth.exceptions.ResourceNotFoundException;
import com.example.auth.models.LocalUser;
import com.example.auth.models.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("localUserDetailService")
public class LocalUserDetailService implements UserDetailsService {
    @Autowired
    private UserService userService;

    @Override
    @Transactional
    public LocalUser loadUserByUsername(final String email) throws UsernameNotFoundException {
        UserEntity user = userService.findUserByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException("User " + email + " was not found in the database");
        }
        return new LocalUser(user);
    }

    @Transactional
    public LocalUser loadUserById(Long id) {
        UserEntity user = userService.findUserById(id).orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
        return new LocalUser(user);
    }

}

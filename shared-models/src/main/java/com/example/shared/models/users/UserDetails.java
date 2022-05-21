package com.example.shared.models.users;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

public class UserDetails extends User {

    private final Long id;

    private final String displayName;

    private final String pictures;

    private final UserType type;

    public UserDetails(Long id, String displayName, String pictures, UserType type, String email, boolean enabled, Collection<? extends GrantedAuthority> authorities) {
        super(email, "NoPassAuth", enabled, true, true, true, authorities);

        this.id = id;
        this.displayName = displayName;
        this.pictures = pictures;
        this.type = type;
    }

    public Long getId() {
        return id;
    }

    public UserInfo toUserInfo() {
        return new UserInfo(id, getUsername(), displayName, pictures, type);
    }
}

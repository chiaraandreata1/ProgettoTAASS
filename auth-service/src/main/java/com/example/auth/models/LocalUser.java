package com.example.auth.models;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.*;
import java.util.stream.Collectors;

public class LocalUser extends User implements OAuth2User, OidcUser {

    private final OidcIdToken idToken;
    private final OidcUserInfo userInfo;
    private final Map<String, Object> attributes;
    private final UserEntity user;

    public LocalUser(UserEntity user) {
        this(user, null, null);
    }

    public LocalUser(UserEntity user, OidcIdToken idToken, OidcUserInfo userInfo) {
        this(user, idToken, userInfo, new HashMap<>());
    }

    public LocalUser(UserEntity user, OidcIdToken idToken, OidcUserInfo userInfo, Map<String, Object> attributes) {

        super(user.getEmail(), "noPassAuth", user.isEnabled(), true, true, true, buildSimpleGrantedAuthorities(user.getRoles()));

        this.user = user;
        this.idToken = idToken;
        this.userInfo = userInfo;
        this.attributes = attributes;
    }

    private static Collection<? extends GrantedAuthority> buildSimpleGrantedAuthorities(Set<UserEntity.Role> roles) {
        return roles
                .stream()
                .map(UserEntity.Role::getAuthoritativeRole)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }


    @Override
    public String getName() {
        return this.user.getDisplayName();
    }

    @Override
    public Map<String, Object> getAttributes() {
        return this.attributes;
    }

    @Override
    public Map<String, Object> getClaims() {
        return this.attributes;
    }

    @Override
    public OidcUserInfo getUserInfo() {
        return this.userInfo;
    }

    @Override
    public OidcIdToken getIdToken() {
        return this.idToken;
    }

    public UserEntity getUser() {
        return user;
    }

    public UserInfo toUserInfo() {
        List<UserEntity.Role> roles =
                getAuthorities()
                        .stream()
                        .map(GrantedAuthority::getAuthority)
                        .map(role -> role.substring(5))
                        .map(UserEntity.Role::valueOf)
                        .collect(Collectors.toList());

        return new UserInfo(user.getId().toString(),
                user.getDisplayName(),
                user.getEmail(),
                user.getPictureLink(),
                roles);
    }
}

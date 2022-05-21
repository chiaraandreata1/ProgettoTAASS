package com.example.auth.models;

import com.example.shared.models.users.UserDetails;
import com.example.shared.models.users.UserInfo;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class LocalUser extends User implements OAuth2User, OidcUser, Serializable {

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

        super(user.getEmail(),
                "noPassAuth",
                user.isEnabled(),
                true,
                true,
                true,
                user.getAuthoritativeRoles());

        this.user = user;
        this.idToken = idToken;
        this.userInfo = userInfo;
        this.attributes = attributes;
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

    public UserDetails toUserDetails() {
        return new UserDetails(
                user.getId(),
                user.getDisplayName(),
                user.getPictureLink(),
                user.getType(),
                user.getEmail(),
                user.isEnabled(),
                user.getAuthoritativeRoles()
        );
    }
}

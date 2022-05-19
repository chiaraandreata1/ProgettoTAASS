package com.example.auth.models;

import com.example.shared.models.users.UserInfo;
import com.example.shared.models.users.UserType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
public class UserEntity implements Serializable {

    public enum Role {
        USER,
        PLAYER,
        TRAINER,
        MANAGEMENT,
        ADMIN;

        public SimpleGrantedAuthority getSimpleGrantedAuthority() {
            return new SimpleGrantedAuthority(String.format("ROLE_%s", name()));
        }

        public static Role fromAuthoritativeRole(String authoritativeRole) {
            return valueOf(authoritativeRole.substring(5));
        }

    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "USER_ID")
    private Long id;

    @Column(name = "PROVIDER_USER_ID")
    private String providerUserId;

    private String email;

    @Column(name = "enabled")
    private boolean enabled;

    @Column(name = "DISPLAY_NAME")
    private String displayName;

    @Column(name = "created_date", nullable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    protected Date createdDate;

    @Temporal(TemporalType.TIMESTAMP)
    protected Date modifiedDate;

    private String provider;

    @Enumerated(EnumType.STRING)
    private UserType type;

    private String pictureLink;

    public UserEntity() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProviderUserId() {
        return providerUserId;
    }

    public void setProviderUserId(String providerUserId) {
        this.providerUserId = providerUserId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(Date modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public String getProvider() {
        return provider;
    }

    public UserType getType() {
        return type;
    }

    public void setType(UserType type) {
        this.type = type;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public Set<SimpleGrantedAuthority> getAuthoritativeRoles() {
        HashSet<Role> roles = new HashSet<>();

        roles.add(Role.USER);

        if (type == UserType.PLAYER)
            roles.add(Role.PLAYER);

        if (type == UserType.TRAINER)
            roles.add(Role.TRAINER);

        if (type == UserType.ADMIN)
            roles.add(Role.ADMIN);

        if (type == UserType.TRAINER || type == UserType.ADMIN)
            roles.add(Role.MANAGEMENT);

        return roles.stream().map(Role::getSimpleGrantedAuthority).collect(Collectors.toSet());
    }

    public String getPictureLink() {
        return pictureLink;
    }

    public void setPictureLink(String pictureLink) {
        this.pictureLink = pictureLink;
    }

    public void update(OAuth2UserInfo userInfo) {
        this.displayName = userInfo.getName();
        this.pictureLink = userInfo.getImageUrl();
    }

    public UserInfo toUserInfo() {
        return new UserInfo(
                id,
                email,
                displayName,
                pictureLink,
                type
        );
    }
}
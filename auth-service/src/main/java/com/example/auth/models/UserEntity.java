package com.example.auth.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Set;

@Entity
public class UserEntity implements Serializable {

    public enum Role {
        USER(),
        PLAYER (),
        TRAINER (),
        ADMIN ();

        Role() {
        }

        public String getAuthoritativeRole() {
            return String.format("ROLE_%s", name());
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


    @JsonIgnore
    @Enumerated
    @ElementCollection
    private Set<Role> roles;
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

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
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
}
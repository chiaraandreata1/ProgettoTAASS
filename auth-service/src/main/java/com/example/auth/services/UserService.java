package com.example.auth.services;

import com.example.auth.exceptions.ExistingEmailAuthenticationException;
import com.example.auth.exceptions.OAuth2AuthenticationProcessingException;
import com.example.auth.models.LocalUser;
import com.example.auth.models.OAuth2UserInfo;
import com.example.auth.models.SocialProvider;
import com.example.auth.models.UserEntity;
import com.example.auth.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.*;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Transactional(value = "transactionManager")
    public UserEntity registerNewUser(final SocialProvider socialProvider, OAuth2UserInfo oAuth2UserInfo) {
        if (userRepository.existsByEmail(oAuth2UserInfo.getEmail())) {
            throw new ExistingEmailAuthenticationException(oAuth2UserInfo.getEmail());
        }

        UserEntity user = new UserEntity();

        user.setDisplayName(oAuth2UserInfo.getName());
        user.setEmail(oAuth2UserInfo.getEmail());

        final HashSet<UserEntity.Role> roles = new HashSet<>();
        roles.add(UserEntity.Role.USER);
        roles.add(UserEntity.Role.PLAYER);
        if (userRepository.count() == 0)
            roles.add(UserEntity.Role.ADMIN);

        user.setRoles(roles);
        user.setProvider(socialProvider.getProviderType());
        user.setEnabled(true);
        user.setProviderUserId(oAuth2UserInfo.getId());

        Date now = Calendar.getInstance().getTime();
        user.setCreatedDate(now);
        user.setModifiedDate(now);
        user = userRepository.save(user);
        userRepository.flush();

        return user;
    }

    public UserEntity findUserByEmail(final String email) {
        return userRepository.findByEmail(email);
    }

    @Transactional
    public LocalUser processUserRegistration(String registrationId, Map<String, Object> attributes, OidcIdToken idToken, OidcUserInfo userInfo) {
        SocialProvider provider = SocialProvider.search(registrationId);

        if (provider == null)
            throw new OAuth2AuthenticationProcessingException("Sorry! Login with " + registrationId + " is not supported yet.");


        OAuth2UserInfo oAuth2UserInfo = provider.inflate(attributes);

        if (!StringUtils.hasText(oAuth2UserInfo.getName())) {
            throw new OAuth2AuthenticationProcessingException("Name not found from OAuth2 provider");
        } else if (!StringUtils.hasText(oAuth2UserInfo.getEmail())) {
            throw new OAuth2AuthenticationProcessingException("Email not found from OAuth2 provider");
        }

        UserEntity user = findUserByEmail(oAuth2UserInfo.getEmail());

        if (user == null)
            user = registerNewUser(provider, oAuth2UserInfo);
        else if (!user.getProvider().equals(registrationId))
            throw new OAuth2AuthenticationProcessingException(user);
        else
            user.update(oAuth2UserInfo);


        return new LocalUser(user, idToken, userInfo, attributes);
    }

    public Optional<UserEntity> findUserById(Long id) {
        return userRepository.findById(id);
    }


}
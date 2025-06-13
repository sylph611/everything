package org.sylph.everything.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.sylph.everything.entity.User;
import org.sylph.everything.repository.UserRepository;

import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oauth2User = super.loadUser(userRequest);
        
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        Map<String, Object> attributes = oauth2User.getAttributes();
        
        User user = saveOrUpdateUser(registrationId, attributes);
        
        return new CustomOAuth2User(oauth2User, user);
    }

    private User saveOrUpdateUser(String registrationId, Map<String, Object> attributes) {
        String email = getEmail(registrationId, attributes);
        String name = getName(registrationId, attributes);
        String picture = getPicture(registrationId, attributes);
        String providerId = getProviderId(registrationId, attributes);

        Optional<User> existingUser = userRepository.findByProviderAndProviderId(registrationId, providerId);
        
        if (existingUser.isPresent()) {
            User user = existingUser.get();
            user.setName(name);
            user.setPicture(picture);
            user.setEmail(email);
            return userRepository.save(user);
        } else {
            User newUser = new User();
            newUser.setEmail(email);
            newUser.setName(name);
            newUser.setPicture(picture);
            newUser.setProvider(registrationId);
            newUser.setProviderId(providerId);
            return userRepository.save(newUser);
        }
    }

    private String getEmail(String registrationId, Map<String, Object> attributes) {
        if ("google".equals(registrationId)) {
            return (String) attributes.get("email");
        } else if ("github".equals(registrationId)) {
            return (String) attributes.get("email");
        }
        return null;
    }

    private String getName(String registrationId, Map<String, Object> attributes) {
        if ("google".equals(registrationId)) {
            return (String) attributes.get("name");
        } else if ("github".equals(registrationId)) {
            return (String) attributes.get("name");
        }
        return null;
    }

    private String getPicture(String registrationId, Map<String, Object> attributes) {
        if ("google".equals(registrationId)) {
            return (String) attributes.get("picture");
        } else if ("github".equals(registrationId)) {
            return (String) attributes.get("avatar_url");
        }
        return null;
    }

    private String getProviderId(String registrationId, Map<String, Object> attributes) {
        if ("google".equals(registrationId)) {
            return String.valueOf(attributes.get("sub"));
        } else if ("github".equals(registrationId)) {
            return String.valueOf(attributes.get("id"));
        }
        return null;
    }
}
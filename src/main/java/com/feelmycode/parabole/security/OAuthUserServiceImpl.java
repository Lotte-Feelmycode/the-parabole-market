package com.feelmycode.parabole.security;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.feelmycode.parabole.domain.User;
import com.feelmycode.parabole.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class OAuthUserServiceImpl extends DefaultOAuth2UserService {
// public class PrincipalOauth2UserService extends DefaultOAuth2UserService { 와 동일
    @Autowired
    private UserRepository userRepository;

    public OAuthUserServiceImpl() {
        super();
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        final OAuth2User oAuth2User = super.loadUser(userRequest);

        try {
            log.info("OAuth2User attributes {} ", new ObjectMapper().writeValueAsString(oAuth2User.getAttributes()));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        final String googleSub = (String) oAuth2User.getAttributes().get("sub");
        final String googleName = (String) oAuth2User.getAttributes().get("name");
        final String googlePic = (String) oAuth2User.getAttributes().get("picture");
        final String googleEmail = (String) oAuth2User.getAttributes().get("email");

        log.info("googleEmail : " + googleEmail);
        final String authProvider = userRequest.getClientRegistration().getClientName();
        User user = null;

        if(!userRepository.existsByEmail(googleEmail)) {
            user = User.builder()
                .email(googleEmail)
                .username(googleName)
                .imageUrl(googlePic)
                .nickname("")
                .authProvider(authProvider)
                .build();
            user = userRepository.save(user);
        } else {
            user = userRepository.findByEmail(googleEmail);
        }

        log.info("Successfully pulled user info username {} authProvider {}",
            googleEmail,
            authProvider);

        return new ApplicationOAuth2User(user.getId().toString(), oAuth2User.getAttributes());
    }
}

package com.feelmycode.parabole.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.feelmycode.parabole.domain.KakaoOauthToken;
import com.feelmycode.parabole.domain.KakaoProfile;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.feelmycode.parabole.domain.NaverOauthToken;
import com.feelmycode.parabole.domain.NaverProfile;
import com.feelmycode.parabole.domain.GoogleOauthToken;
import com.feelmycode.parabole.domain.GoogleProfile;
import com.feelmycode.parabole.domain.Seller;
import com.feelmycode.parabole.domain.User;
import com.feelmycode.parabole.dto.UserDto;
import com.feelmycode.parabole.dto.UserInfoResponseDto;
import com.feelmycode.parabole.dto.UserLoginResponseDto;
import com.feelmycode.parabole.dto.UserSearchDto;
import com.feelmycode.parabole.global.error.exception.NoSuchAccountException;
import com.feelmycode.parabole.global.error.exception.ParaboleException;
import com.feelmycode.parabole.global.util.JwtUtils;
import com.feelmycode.parabole.global.util.StringUtil;
import com.feelmycode.parabole.repository.UserRepository;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final JwtUtils jwtUtils;
    private final UserRepository userRepository;
    private final CartService cartService;
    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Value("${sns.kakao.client-id}")
    private String kakaoClientId;
//    @Value("${sns.kakao.client-secret}")
//    private String kakaoClientSecret;
    @Value("${sns.kakao.redirect-uri}")
    private String kakaoRedirectUri;
    @Value("${sns.naver.client-id}")
    private String naverClientId;
    @Value("${sns.naver.client-secret}")
    private String naverClientSecret;
    @Value("${sns.naver.redirect-uri}")
    private String naverRedirectUri;
    @Value("${sns.google.client-id}")
    private String googleClientId;
    @Value("${sns.google.client-secret}")
    private String googleClientSecret;
    @Value("${sns.google.redirect-uri}")
    private String googleRedirectUri;

    @Transactional
    public UserDto create(UserDto userDTO) {
        String email = userDTO.getEmail();
        if(StringUtil.controllerParamIsBlank(email)) {
            throw new ParaboleException(HttpStatus.BAD_REQUEST, "이메일을 입력하세요.");
        }
        if(userRepository.existsByEmail(email)) {
            log.warn("Email already exists {}", email);
            throw new ParaboleException(HttpStatus.BAD_REQUEST, "이미 사용중인 이메일입니다.");
        }

        User user = User.builder()
            .email(userDTO.getEmail())
            .username(userDTO.getName())
            .nickname(userDTO.getNickname())
            .phone(userDTO.getPhone())
            .password(passwordEncoder.encode(userDTO.getPassword()))
            .imageUrl("https://ssl.pstatic.net/static/cafe/cafe_pc/default/cafe_profile_77.png")
            .role("ROLE_USER")
            .authProvider("Home")
            .build();
        User newUser = userRepository.save(user);

        cartService.createCart(newUser.getId());

        return UserDto.builder()
            .id(newUser.getId())
            .name(newUser.getUsername())
            .nickname(newUser.getNickname())
            .build();       // welcome page 위한 부분
    }

    public UserLoginResponseDto getByCredentials(UserDto userDto) {
        User originalUser = userRepository.findByEmail(userDto.getEmail());

        if(originalUser != null && passwordEncoder.matches(userDto.getPassword(), originalUser.getPassword())) {
            String token = jwtUtils.generateToken(originalUser);
            log.info("generated Token {}", token);

            return new UserLoginResponseDto(originalUser, token);
        } else {
            throw new NoSuchAccountException();
        }
    }

    public boolean isSeller(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new NoSuchAccountException())
            .getRole().equals("ROLE_SELLER");
    }

    public Seller getSeller(Long userId) {
        return getUser(userId).getSeller();
    }
    public UserInfoResponseDto getUserInfo(Long userId) {

        User user = getUser(userId);
        if(user.getRole().equals("ROLE_USER")){
            return new UserInfoResponseDto(user.getEmail(), user.getUsername(), user.getNickname(), "USER", user.getPhone());
        }
        return new UserInfoResponseDto(user.getEmail(), user.getUsername(), user.getNickname(), "SELLER", user.getPhone());
    }
    public User getUser(Long userId) {
        return userRepository.findById(userId).orElseThrow(
            () -> new ParaboleException(HttpStatus.NOT_FOUND, "해당 사용자Id로 조회되는 사용자가 존재하지 않습니다."));
    }

    public List<UserSearchDto> getNonSellerUsers(String userName) {

        List<User> list;
        if (userName.equals("")) {
            list = userRepository.findAll();
        } else {
            list = userRepository.findAllByUsernameContainsIgnoreCase(userName);
        }
        List<UserSearchDto> dtos = new ArrayList<>();
        for (User u : list) {
            if (u.sellerIsNull()) {
                dtos.add(new UserSearchDto(u.getId(), u.getUsername(), u.getEmail(),
                    u.getPhone()));
            }
        }
        if (dtos.isEmpty()) {
            throw new ParaboleException(HttpStatus.NOT_FOUND, "사용자가 존재하지 않습니다.");
        }
        return dtos;
    }

        public GoogleOauthToken getAccessTokenGoogle(String code) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("code", code);
        params.add("client_id", googleClientId);
        params.add("client_secret", googleClientSecret);
        params.add("redirect_uri", googleRedirectUri);
        params.add("grant_type", "authorization_code");
        HttpEntity<MultiValueMap<String, String>> googleTokenRequest =
            new HttpEntity<>(params, headers);

        ResponseEntity<String> accessTokenResponse = restTemplate.exchange(
            "https://oauth2.googleapis.com/token",
            HttpMethod.POST,
            googleTokenRequest,
            String.class
        );

        ObjectMapper objectMapper = new ObjectMapper();
        GoogleOauthToken googleOauthToken = null;
        try {
            googleOauthToken = objectMapper.readValue(accessTokenResponse.getBody(), GoogleOauthToken.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
//        log.info(">>>>>>> googleOauthToken {}", googleOauthToken);
        return googleOauthToken;
    }

    @Transactional
    public UserLoginResponseDto saveUserAndGetTokenGoogle(String token) { // 발급 받은 accessToken 으로 카카오 회원 정보 DB 저장 후 JWT 를 생성
        GoogleProfile profile = findProfileGoogle(token);
        log.info(" >>>>>>>>>>>> Google Profile {}", profile.toString());

        User user = userRepository.findByEmail(profile.getEmail());
        if(user == null) {
            user = User.builder()
//                .id(profile.getId())
                .imageUrl(profile.getPicture())
                .email(profile.getEmail())
                .username(profile.getName())
                .nickname(profile.getName())
                .authProvider("Google")
                .role("ROLE_USER").build();

            cartService.createCart(userRepository.save(user).getId());
        }
        String userToken =  jwtUtils.generateToken(user);
        log.info(" >>>>>>>>>>>> Generated Custom Jwt token {}", userToken);

        return UserLoginResponseDto.builder().userId(user.getId()).email(user.getEmail())
            .name(user.getUsername()).nickname(user.getNickname()).token(userToken).role(user.getRole())
            .imageUrl(user.getImageUrl()).authProvider("Google").build();
    }

    public GoogleProfile findProfileGoogle(String token) {

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + token); //(1-4)
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        HttpEntity<MultiValueMap<String, String>> googleProfileRequest = new HttpEntity<>(headers);

        ResponseEntity<String> googleProfileResponse = restTemplate.exchange(
            "https://www.googleapis.com/oauth2/v2/userinfo?alt=json",
            HttpMethod.GET,
            googleProfileRequest,
            String.class
        );
        log.info("ResponseEntity<String> googleProfileResponse RestTemplate {}",googleProfileResponse.toString());

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        GoogleProfile googleProfile = null;
        try {
            googleProfile = objectMapper.readValue(googleProfileResponse.getBody(), GoogleProfile.class);

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
//        log.info(" >>>>>>>>>>>> GoogleProfile Form {}", googleProfile.toString());
        return googleProfile;
    }
    
    public NaverOauthToken getAccessTokenNaver(String code, String state) {

        RestTemplate restTemplate = new RestTemplate();
//        restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("code", code);
        params.add("state", state);
        params.add("client_id", naverClientId);
        params.add("redirect_uri", naverRedirectUri);
        params.add("client_secret", naverClientSecret);

        HttpEntity<MultiValueMap<String, String>> naverTokenRequest =
            new HttpEntity<>(params, headers);

        ResponseEntity<String> accessTokenResponse = restTemplate.exchange(
            "https://nid.naver.com/oauth2.0/token",
            HttpMethod.POST,
            naverTokenRequest,
            String.class
        );

        ObjectMapper objectMapper = new ObjectMapper();
        NaverOauthToken naverOauthToken = null;
        try {
            naverOauthToken = objectMapper.readValue(accessTokenResponse.getBody(), NaverOauthToken.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return naverOauthToken;                 // (5) 카카오 -> be 로 발급해준 accessToken
    }

    @Transactional
    public UserLoginResponseDto saveUserAndGetTokenNaver(String token) {
        NaverProfile profile = findProfileNaver(token);

        User user = userRepository.findByEmail(profile.getResponse().getEmail());
        if(user == null) {
            user = User.builder()
//                .id(profile.response.getId())
                .imageUrl(profile.getResponse().getProfile_image())
                .username(profile.getResponse().getName())
                .nickname(profile.getResponse().getNickname())
//                .username(name)
//                .nickname(nickname)
                .email(profile.getResponse().getEmail())
                .authProvider("Naver")
                .phone(profile.getResponse().getMobile())
                .role("ROLE_USER").build();

            cartService.createCart(userRepository.save(user).getId());
        }
        String userToken =  jwtUtils.generateToken(user);

        return UserLoginResponseDto.builder().userId(user.getId()).email(user.getEmail()).role(user.getRole())
            .name(user.getUsername()).nickname(user.getNickname()).token(userToken).phone(user.getPhone())
            .imageUrl(user.getImageUrl()).authProvider("Naver").build();
    }

    public NaverProfile findProfileNaver(String token) {

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + token);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        HttpEntity<MultiValueMap<String, String>> naverProfileRequest = new HttpEntity<>(headers);

        ResponseEntity<String> naverProfileResponse = restTemplate.exchange(
            "https://openapi.naver.com/v1/nid/me",
            HttpMethod.GET,
            naverProfileRequest,
            String.class
        );
        log.info("naverProfileResponse RestTemplate{}",naverProfileResponse.toString());    // 한글 깨짐 발생

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        NaverProfile naverProfile = null;
        try {
            naverProfile = objectMapper.readValue(naverProfileResponse.getBody(), NaverProfile.class);

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        log.info("naverProfileResponse RestTemplate{}",naverProfile.toString());            // 한글 정상

        return naverProfile;
    }

    public KakaoOauthToken getAccessTokenKakao(String code) {     // (3) fe->be인가 코드 전달, (4) be->카카오 인가코드로 엑세스 토큰 요청

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("code", code);
        params.add("client_id", kakaoClientId);
        params.add("redirect_uri", kakaoRedirectUri);
//        params.add("client_secret", kakaoClientSecret);

        HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest =
            new HttpEntity<>(params, headers);

        ResponseEntity<String> accessTokenResponse = restTemplate.exchange(
            "https://kauth.kakao.com/oauth/token",
            HttpMethod.POST,
            kakaoTokenRequest,
            String.class
        );

        ObjectMapper objectMapper = new ObjectMapper();
        KakaoOauthToken kakaoOauthToken = null;
        try {
            kakaoOauthToken = objectMapper.readValue(accessTokenResponse.getBody(), KakaoOauthToken.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return kakaoOauthToken;                 // (5) 카카오 -> be 로 발급해준 accessToken
    }

    @Transactional
    public UserLoginResponseDto saveUserAndGetTokenKakao(String token) { // 발급 받은 accessToken 으로 카카오 회원 정보 DB 저장 후 JWT 를 생성
        KakaoProfile profile = findProfileKakao(token);
        log.info(">>>>>>>>>>>> KakaoProfile sent from Kakao (Before Custom) {}", profile.toString());

        User user = userRepository.findByEmail(profile.getKakao_account().getEmail());
        if(user == null) {
            user = User.builder()
//                .id(profile.getId())
                .imageUrl(profile.getKakao_account().getProfile().getProfile_image_url())
                .username(profile.getKakao_account().getProfile().getNickname())
                .nickname(profile.getKakao_account().getProfile().getNickname())
                .email(profile.getKakao_account().getEmail())
                .authProvider("Kakao")
                .role("ROLE_USER").build();

            cartService.createCart(userRepository.save(user).getId());
        }
        String userToken =  jwtUtils.generateToken(user);

        return UserLoginResponseDto.builder().userId(user.getId()).email(user.getEmail())
            .name(user.getUsername()).nickname(user.getNickname()).token(userToken).role(user.getRole())
            .imageUrl(user.getImageUrl()).authProvider("Kakao").build();

    }

    public KakaoProfile findProfileKakao(String token) {

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + token); //(1-4)
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        HttpEntity<MultiValueMap<String, String>> kakaoProfileRequest = new HttpEntity<>(headers);

        ResponseEntity<String> kakaoProfileResponse = restTemplate.exchange(
            "https://kapi.kakao.com/v2/user/me",
            HttpMethod.POST,
            kakaoProfileRequest,
            String.class
        );

        ObjectMapper objectMapper = new ObjectMapper();
        KakaoProfile kakaoProfile = null;
        try {
            kakaoProfile = objectMapper.readValue(kakaoProfileResponse.getBody(), KakaoProfile.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return kakaoProfile;
    }
    
}

package com.feelmycode.parabole.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.feelmycode.parabole.domain.Seller;
import com.feelmycode.parabole.domain.User;
import com.feelmycode.parabole.dto.UserInfoResponseDto;
import com.feelmycode.parabole.dto.UserSearchDto;
import com.feelmycode.parabole.global.error.exception.ParaboleException;
import com.feelmycode.parabole.repository.UserRepository;
import com.feelmycode.parabole.security.model.KakaoOauthToken;
import com.feelmycode.parabole.security.model.KakaoProfile;
import com.feelmycode.parabole.security.model.NaverOauthToken;
import com.feelmycode.parabole.security.model.NaverProfile;
import com.feelmycode.parabole.security.utils.TokenProvider;
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

    private final TokenProvider tokenProvider;
    private final UserRepository userRepository;
    private final CartService cartService;

    @Transactional
    public User create(final User user) {
        if(user == null || user.getEmail() == null ) {
            throw new ParaboleException(HttpStatus.BAD_REQUEST, "잘못된 이메일입니다.");
        }
        final String email = user.getEmail();
        if(userRepository.existsByEmail(email)) {
            log.warn("Email already exists {}", email);
            throw new ParaboleException(HttpStatus.BAD_REQUEST, "이미 사용중인 이메일입니다.");
        }

        return userRepository.save(user);
    }

    public User getByCredentials(final String email, final String password, final PasswordEncoder encoder) {
        final User originalUser = userRepository.findByEmail(email);

        if(originalUser != null && encoder.matches(password, originalUser.getPassword())) {
            return originalUser;
        }
        return null;
    }
    
//    @Transactional
//    public User signup(@NotNull UserSignupDto dto) {
//
//        if (dto.checkIfBlankExists()) {
//            throw new ParaboleException(HttpStatus.BAD_REQUEST, "회원가입 입력란에 채우지 않은 란이 있습니다.");
//        }
//        if (!dto.getPassword().equals(dto.getPasswordConfirmation())) {
//            throw new ParaboleException(HttpStatus.BAD_REQUEST, "회원가입 시에 입력한 비밀번호와 비밀번호 확인란이 일치하지 않습니다.");
//        }
//        User user = userRepository.findByEmail(dto.getEmail());
//        if (user != null) {
//            throw new ParaboleException(HttpStatus.BAD_REQUEST, "회원가입 시에 입력하신 이메일을 사용 중인 유저가 존재합니다. 다른 이메일로 가입해주세요.");
//        }
//
//        User save = userRepository.save(
//            dto.toEntity(dto.getEmail(), dto.getUsername(), dto.getNickname(), dto.getPhone(),
//                dto.getPassword()));
//        Long cartId = cartService.createCart(save.getId());
//        log.info("{} - 카트 생성완료: {}", save.getId(), cartId);
//        return save;
//    }
//
//    public User signin(@NotNull UserSigninDto dto) {
//        log.info("email: {}, password: {}", dto.getEmail(), dto.getPassword());
//        if (dto.getEmail().equals("") || dto.getPassword().equals("")) {
//            throw new ParaboleException(HttpStatus.BAD_REQUEST, "로그인 입력란에 채우지 않은 란이 있습니다.");
//        }
//        User user = userRepository.findByEmail(dto.getEmail());
//        if (user == null) {
//            throw new ParaboleException(HttpStatus.BAD_REQUEST, "입력하신 이메일을 가진 사용자가 존재하지 않습니다");
//        }
//        if (!user.getPassword().equals(dto.getPassword())) {
//            throw new ParaboleException(HttpStatus.BAD_REQUEST, "이메일 또는 비밀번호가 일치하지 않습니다. 다시 입력해 주세요");
//        }
//        return user;
//    }

    public boolean isSeller(Long userId) {
        return !getUser(userId).sellerIsNull();
    }

    public Seller getSeller(Long userId) {
        return getUser(userId).getSeller();
    }

    public UserInfoResponseDto getUserInfo(Long userId) {

        User user = getUser(userId);
        if(user.sellerIsNull()){
            return new UserInfoResponseDto(user.getEmail(), user.getUsername(), user.getNickname(), "USER", user.getPhone());
        }
        return new UserInfoResponseDto(user.getEmail(), user.getUsername(), user.getNickname(), "SELLER", user.getPhone());
    }

    public User getUser(Long userId) {
        return userRepository.findById(userId).orElseThrow(
            () -> new ParaboleException(HttpStatus.NOT_FOUND, "해당 사용자Id로 조회되는 사용자가 존재하지 않습니다."));
    }

    public void changeRoleToSeller(User user, Seller seller) {
        user.setSeller(seller);
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

    @Value("${spring.security.oauth2.client.registration.kakao.client-id}")
    private String kakaoClientId;
    @Value("${spring.security.oauth2.client.registration.kakao.redirect-uri}")
    private String kakaoRedirectUri;
    @Value("${spring.security.oauth2.client.registration.kakao.client-secret}")
    private String kakaoClientSecret;

    public KakaoOauthToken getAccessTokenKakao(String code) {     // (3) fe->be인가 코드 전달, (4) be->카카오 인가코드로 엑세스 토큰 요청

        RestTemplate restTemplate = new RestTemplate();
//        restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());

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

    @Value("${spring.security.oauth2.client.registration.naver.client-id}")
    private String naverClientId;
    @Value("${spring.security.oauth2.client.registration.naver.redirect-uri}")
    private String naverRedirectUri;
    @Value("${spring.security.oauth2.client.registration.naver.client-secret}")
    private String naverClientSecret;

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

    public KakaoProfile findProfileKakao(String token) {

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + token); //(1-4)
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        HttpEntity<MultiValueMap<String, String>> kakaoProfileRequest =
            new HttpEntity<>(headers);

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
        log.info("kakaoProfile {}", kakaoProfile);
        return kakaoProfile;
    }

    @Transactional
    public String saveUserAndGetTokenKakao(String token) { // 발급 받은 accessToken 으로 카카오 회원 정보 DB 저장 후 JWT 를 생성
        KakaoProfile profile = findProfileKakao(token);
        log.info(profile.toString());

        User user = userRepository.findByEmail(profile.getKakao_account().getEmail());
        if(user == null) {
            user = User.builder()
                .id(profile.getId())
                .imageUrl(profile.getKakao_account().getProfile().getProfile_image_url())
                .nickname(profile.getKakao_account().getProfile().getNickname())
                .email(profile.getKakao_account().getEmail())
                .authProvider("Kakao")
                .username(profile.getKakao_account().getProfile().getNickname())
//                .phone(user.getPhone())
                .role("ROLE_USER").build();

            userRepository.save(user);
        }
        return tokenProvider.create(user);
    }

    public NaverProfile findProfileNaver(String token) {

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + token); //(1-4)
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        HttpEntity<MultiValueMap<String, String>> naverProfileRequest =
            new HttpEntity<>(headers);

        // Http 요청 (POST 방식) 후, response 변수에 응답을 받음
        ResponseEntity<String> naverProfileResponse = restTemplate.exchange(
            "https://openapi.naver.com/v1/nid/me",
            HttpMethod.GET,
            naverProfileRequest,
            String.class
        );

        log.info("naverProfileResponse {}",naverProfileResponse.toString());

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
//        이 설정을 통해 JSON의 모든 데이터를 파싱하는 것이 아닌 내가 필요로 하는 데이터, 즉 내가 필드로 선언한 데이터들만 파싱할 수 있다.

        NaverProfile naverProfile = null;
        try {
            naverProfile = objectMapper.readValue(naverProfileResponse.getBody(), NaverProfile.class);
            System.out.println(naverProfile.getResultcode());
            System.out.println(naverProfile.getMessage());
            System.out.println(naverProfile.getResponse());

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return naverProfile;
    }

    @Transactional
    public String saveUserAndGetTokenNaver(String token) {
        NaverProfile profile = findProfileNaver(token);

        User user = userRepository.findByEmail(profile.response.getEmail());
        if(user == null) {
            user = User.builder()
//                .id(profile.response.getId())
                .imageUrl(profile.getResponse().getProfile_image())
                .nickname(profile.getResponse().getNickname())
                .email(profile.getResponse().getEmail())
                .authProvider("Naver")
                .username(profile.getResponse().getNickname())
                .phone(profile.getResponse().getMobile())
                .role("ROLE_USER").build();

            userRepository.save(user);
        }
        return tokenProvider.create(user);
    }

}

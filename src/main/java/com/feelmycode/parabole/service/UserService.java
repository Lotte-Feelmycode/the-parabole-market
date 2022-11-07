package com.feelmycode.parabole.service;

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
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final JwtUtils jwtUtils;
    private final UserRepository userRepository;
    private final CartService cartService;
    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();


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

}

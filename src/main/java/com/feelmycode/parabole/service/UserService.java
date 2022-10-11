package com.feelmycode.parabole.service;


import com.feelmycode.parabole.domain.Seller;
import com.feelmycode.parabole.domain.User;
import com.feelmycode.parabole.dto.UserInfoResponseDto;
import com.feelmycode.parabole.dto.UserSearchDto;
import com.feelmycode.parabole.dto.UserSigninDto;
import com.feelmycode.parabole.dto.UserSignupDto;
import com.feelmycode.parabole.global.error.exception.NoDataException;
import com.feelmycode.parabole.global.error.exception.ParaboleException;
import com.feelmycode.parabole.repository.UserRepository;
import java.util.ArrayList;
import java.util.List;
import javax.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public User signup(@NotNull UserSignupDto dto) {

        if (dto.checkIfBlankExists()) {
            throw new ParaboleException(HttpStatus.BAD_REQUEST, "회원가입 입력란에 채우지 않은 란이 있습니다.");
        }
        if (!dto.getPassword().equals(dto.getPasswordConfirmation())) {
            throw new ParaboleException(HttpStatus.BAD_REQUEST, "회원가입 시에 입력한 비밀번호와 비밀번호 확인란이 일치하지 않습니다.");
        }
        User user = userRepository.findByEmail(dto.getEmail());
        if (user != null) {
            throw new ParaboleException(HttpStatus.BAD_REQUEST, "회원가입 시에 입력하신 이메일을 사용 중인 유저가 존재합니다. 다른 이메일로 가입해주세요.");
        }
        return userRepository.save(
            dto.toEntity(dto.getEmail(), dto.getUsername(), dto.getNickname(), dto.getPhone(), dto.getPassword()));
    }

    public User signin(@NotNull UserSigninDto dto) {
        log.info("email: {}, password: {}", dto.getEmail(), dto.getPassword());
        if (dto.getEmail().equals("") || dto.getPassword().equals("")) {
            throw new ParaboleException(HttpStatus.BAD_REQUEST, "로그인 입력란에 채우지 않은 란이 있습니다.");
        }
        User user = userRepository.findByEmail(dto.getEmail());
        if (user == null) {
            throw new ParaboleException(HttpStatus.BAD_REQUEST, "입력하신 이메일을 가진 사용자가 존재하지 않습니다");
        }
        if (!user.getPassword().equals(dto.getPassword())) {
            throw new ParaboleException(HttpStatus.BAD_REQUEST, "이메일 또는 비밀번호가 일치하지 않습니다. 다시 입력해 주세요");
        }
        return user;
    }

    public boolean isSeller(Long userId) {
        return !getUser(userId).sellerIsNull();
    }

    public Seller getSeller(Long userId) {
        return getUser(userId).getSeller();
    }

    public UserInfoResponseDto getUserInfo(Long userId) {

        User user = getUser(userId);
        if(user.sellerIsNull()){
            return new UserInfoResponseDto(user.getEmail(), user.getName(), user.getNickname(), "USER");
        }
        return new UserInfoResponseDto(user.getEmail(), user.getName(), user.getNickname(), "SELLER");
    }

    public User getUser(Long userId) {
        return userRepository.findById(userId).orElseThrow(
            () -> new ParaboleException(HttpStatus.NOT_FOUND, "해당 사용자Id로 조회되는 사용자가 존재하지 않습니다."));
    }

    public void changeRoleToSeller(User user, Seller seller) {
        user.setSeller(seller);
    }

    public List<UserSearchDto> getAllNonSellerUsers() {
        List<User> list = userRepository.findAll();
        System.out.println(list.size());
        List<UserSearchDto> dtos = new ArrayList<>();

        for (User u : list) {
            if (u.sellerIsNull()) {
                dtos.add(new UserSearchDto(u.getId(), u.getName(), u.getEmail(),
                    u.getPhone()));
            }
        }
        if (dtos.isEmpty()) {
            throw new ParaboleException(HttpStatus.NOT_FOUND, "USER 역할의 사용자가 존재하지 않습니다.");
        }
        return dtos;
    }

}

package com.feelmycode.parabole.service;


import com.feelmycode.parabole.domain.User;
import com.feelmycode.parabole.dto.UserInfoResponseDto;
import com.feelmycode.parabole.dto.UserSigninDto;
import com.feelmycode.parabole.dto.UserSignupDto;
import com.feelmycode.parabole.global.error.exception.ParaboleException;
import com.feelmycode.parabole.repository.UserRepository;
import javax.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
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
            dto.toEntity(dto.getEmail(), dto.getUsername(), dto.getNickname(), dto.getPassword()));
    }

    // TODO: Repo에서 springdatajpa 사용하여 String 으로 도메인 받아오는 과정 해결x => API 미완성
    @Transactional(readOnly = true)
    public boolean signin(@NotNull UserSigninDto dto) {

        if (dto.getEmail().equals("") || dto.getPassword().equals("")) {
            throw new ParaboleException(HttpStatus.BAD_REQUEST, "로그인 입력란에 채우지 않은 란이 있습니다.");
        }
        if (userRepository.findByEmail(dto.getEmail()) == null) {
            throw new ParaboleException(HttpStatus.BAD_REQUEST, "입력하신 이메일을 가진 사용자가 존재하지 않습니다");
        }
        if (userRepository.findByEmail(dto.getEmail()) != null
            && !userRepository.findByEmail(dto.getEmail()).getPassword().equals(dto.getPassword())) {
            throw new ParaboleException(HttpStatus.BAD_REQUEST, "이메일 또는 비밀번호가 일치하지 않습니다. 다시 입력해 주세요");
        }
        return true;
    }

    @Transactional
    public Integer checkAccountRole(String email) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new ParaboleException(HttpStatus.NOT_FOUND, "해당 이메일을 사용하는 계정이 존재하지 않습니다.");
        }
        if (user.getSeller() == null)
            return 1;       // USER
        return 2;       // SELLER
    }

    @Transactional
    public void deleteUser(Long userId) {
        // 탈퇴 로직이 아니고 user 삭제 입니다. deleteUserWhenSellerCreationFails 역할 수행
        userRepository.deleteById(userId);
    }

    @Transactional(readOnly = true)
    public UserInfoResponseDto myPageUserInfo(Long userId) {

        User user = userRepository.findById(userId).orElseThrow(
            () -> new ParaboleException(HttpStatus.NOT_FOUND, "해당 사용자Id 를 가진 사용자가 존재하지 않습니다"));

        String role = "SELLER";
        if(user.getSeller() == null)
            role = "USER";

        return new UserInfoResponseDto(user.getEmail(), user.getName(), user.getNickname(), role);
    }

}

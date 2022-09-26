package com.feelmycode.parabole.service;


import com.feelmycode.parabole.domain.User;
import com.feelmycode.parabole.dto.UserSigninDto;
import com.feelmycode.parabole.dto.UserSignupDto;
import com.feelmycode.parabole.global.error.exception.ParaboleException;
import com.feelmycode.parabole.repository.UserRepository;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;

    public void signup(@NotNull UserSignupDto dto) {

        if (dto.getEmail().equals("") || dto.getUsername().equals("") ||
            dto.getNickname().equals("") || dto.getPassword().equals("") ||
            dto.getPasswordConfirmation().equals("")) {
            throw new ParaboleException(HttpStatus.BAD_REQUEST,
                "회원가입 입력란에 채우지 않은 란이 있습니다.");
        }
        if (!dto.getPassword().equals(dto.getPasswordConfirmation())) {
            throw new ParaboleException(HttpStatus.BAD_REQUEST,
                "회원가입 시에 입력한 비밀번호와 비밀번호 확인란이 일치하지 않습니다.");
        }
        Optional<User> u = userRepository.findByEmail(dto.getEmail());
        if (u.isPresent()) {
            throw new ParaboleException(HttpStatus.BAD_REQUEST,
                "회원가입 시에 입력하신 이메일을 사용 중인 유저가 존재합니다. 다른 이메일로 가입해주세요.");
        }
        userRepository.save(dto.toEntity());
    }

    public void signin(HttpServletRequest request, @NotNull UserSigninDto dto) {

        if (dto.getEmail().equals("") || dto.getPassword().equals("")) {
            throw new ParaboleException(HttpStatus.BAD_REQUEST,
                "로그인 입력란에 채우지 않은 란이 있습니다.");
        }
        if(userRepository.findByEmail(dto.getEmail()).isEmpty()
            || userRepository.findByPassword(dto.getPassword()).isEmpty()) {
            throw new ParaboleException(HttpStatus.BAD_REQUEST,
                "로그인 시 입력한 이메일 또는 비밀번호를 가진 사용자가 존재하지 않습니다");
        }
        if (!userRepository.findByEmail(dto.getEmail()).orElseThrow(() -> new ParaboleException(
            HttpStatus.BAD_REQUEST, "해당 이메일을 가진 사용자가 존재하지 않습니다."
        )).getPassword().equals(dto.getPassword())) {
            throw new ParaboleException(HttpStatus.BAD_REQUEST,
                "이메일 또는 비밀번호가 일치하지 않습니다. 다시 입력해 주세요");
        }

        HttpSession session = request.getSession();
        if (session.getAttribute("userEmail") != null) {
            throw new ParaboleException(HttpStatus.BAD_REQUEST,
                "이미 로그인 되어있는 사용자가 있어서 로그인 할 수 없습니다.");
        }
        session.setAttribute("userEmail", dto.getEmail());
    }

}

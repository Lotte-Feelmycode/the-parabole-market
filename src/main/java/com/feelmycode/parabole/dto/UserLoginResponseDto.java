package com.feelmycode.parabole.dto;

import com.feelmycode.parabole.domain.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
public class UserLoginResponseDto {
    private Long userId;
    private String email;
    private String name;
    private String nickname;
    private String phone;
    private String imageUrl;
    private String role;
    private String authProvider;
    private Long sellerId;
    private String token;

    @Builder
    public UserLoginResponseDto(Long userId, String email, String name, String nickname, String phone,
        String token, String imageUrl, String role, String authProvider) {
        this.userId = userId;
        this.email = email;
        this.name = name;
        this.nickname = nickname;
        this.phone = phone;
        this.token = token;
        this.imageUrl = imageUrl;
        this.role = role;
        this.authProvider = authProvider;
    }

    public UserLoginResponseDto(User user, String token) {
        this.userId = user.getId();
        this.email = user.getEmail();
        this.name = user.getUsername();
        this.nickname = user.getNickname();
        this.phone = user.getPhone();
        this.imageUrl = user.getImageUrl();
        this.role = user.getRole();
        this.authProvider = user.getAuthProvider();
        if (user.getRole().equals("ROLE_SELLER")) {
            this.sellerId = user.getSeller().getId();
        }
        this.token = token;
    }
}

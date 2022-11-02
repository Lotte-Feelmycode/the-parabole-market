package com.feelmycode.parabole.security.model;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class KakaoOauthToken {
    private String token_type;
    private String access_token;
    private String id_token;    // OpenID Connect 활성화
    private int expires_in;
    private String refresh_token;
    private int refresh_token_expires_in;
    private String scope;
}

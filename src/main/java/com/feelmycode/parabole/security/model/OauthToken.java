package com.feelmycode.parabole.security.model;

import lombok.Data;

@Data
public class OauthToken {
    private String token_type;
    private String access_token;
    private String id_token;    // OpenID Connect 활성화
    private int expires_in;
    private String refresh_token;
    private int refresh_token_expires_in;
    private String scope;

}

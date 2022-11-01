package com.feelmycode.parabole.security.model;

import lombok.Data;

@Data
public class NaverOauthToken {
    private String access_token;
    private String refresh_token;
    private String token_type;
    private int expires_in;
    private String error;
    private String error_description;
}

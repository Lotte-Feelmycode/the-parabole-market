package com.feelmycode.parabole.security.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
public class GoogleOauthToken {

    private String access_token;
    private String expires_in;
    private String scope;
    private String token_type;
    private String id_token;

}

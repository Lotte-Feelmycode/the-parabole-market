package com.feelmycode.parabole.security.model;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class NaverProfile {

    public String resultcode;
    public String message;
    public Response response;

    @Getter
    @NoArgsConstructor
    public class Response {
        public String id;
        public String nickname;
        public String email;
        public String profile_image;
        public String mobile;
    }

}

package com.feelmycode.parabole.security.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
public class NaverProfile {

    public String resultcode;
    public String message;
    public Response response;

    @Getter
    @ToString
    @NoArgsConstructor
    public class Response {
        public String id;
        public String name;
        public String nickname;
        public String email;
        public String profile_image;
        public String mobile;
        public String mobile_e164;

    }

}

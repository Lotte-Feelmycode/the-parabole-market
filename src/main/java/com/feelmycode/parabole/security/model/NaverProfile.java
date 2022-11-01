package com.feelmycode.parabole.security.model;

import lombok.Data;

@Data
public class NaverProfile {

    public String resultcode;
    public String message;
    public Response response;

    @Data
    public class Response { //(2)
        public String email;
        public String nickname;
        public String profile_image;
//        public String age;
//        public String gender;
        public String id;
//        public String name;
//        public String birthday;
//        public String birthyear;
        public String mobile;

    }
}

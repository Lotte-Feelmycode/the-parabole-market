package com.feelmycode.parabole.security.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
public class KakaoProfile {

    public Long id;
    public String connected_at;
    public Properties properties;
    public KakaoAccount kakao_account;

    @Getter
    @ToString
    @NoArgsConstructor
    public class Properties {
        public String nickname;
        public String profile_image;
        public String thumbnail_image;
    }

    @Getter
    @ToString
    @NoArgsConstructor
    public class KakaoAccount {
        public Boolean profile_nickname_needs_agreement;
        public Boolean profile_image_needs_agreement;
        public Profile profile;
        public Boolean has_email;
        public Boolean email_needs_agreement;
        public Boolean is_email_valid;
        public Boolean is_email_verified;
        public String email;

        @Getter
        @ToString
        @NoArgsConstructor
        public class Profile {
            public String nickname;
            public String thumbnail_image_url;
            public String profile_image_url; // 이미지 경로 필드2
            public Boolean is_default_image;

        }
    }
}
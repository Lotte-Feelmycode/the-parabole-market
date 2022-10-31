package com.feelmycode.parabole.domain;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import org.springframework.security.test.context.support.WithSecurityContext;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = WithMockCustomUserSecurityContextFactory.class)
public @interface WithMockCustomUser {
    long id() default 1L;
    String email() default "jul12230103@gmail.com";
    String username() default "cde";
    String nickname() default "cde";
    String phone() default "010-1234-5678";
    String password() default "cde";
    String imageUrl() default "https://www.tibs.org.tw/images/default.jpg";
    String role() default "ROLE_USER";
    String authProvider() default "";

}

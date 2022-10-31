package com.feelmycode.parabole.domain;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

public class WithMockCustomUserSecurityContextFactory implements
    WithSecurityContextFactory<WithMockCustomUser> {

    @Override
    public SecurityContext createSecurityContext(WithMockCustomUser customUser) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        User user = getUser(customUser);
//        MemberDetails principal = MemberDetails.create(user);
        Authentication auth =
            new UsernamePasswordAuthenticationToken(user.getId(), null, AuthorityUtils.NO_AUTHORITIES);
        context.setAuthentication(auth);
        return context;
    }

    private User getUser(WithMockCustomUser customUser) {
        User user = new User().builder().id(customUser.id()).email(customUser.email())
            .password(customUser.password()).username(customUser.username())
            .authProvider(customUser.authProvider())
            .role(customUser.role()).build();
        return user;
    }
}

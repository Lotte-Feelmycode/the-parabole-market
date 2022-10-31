package com.feelmycode.parabole.security;

import static com.feelmycode.parabole.security.RedirectUrlCookieFilter.REDIRECT_URI_PARAM;

import com.feelmycode.parabole.security.utils.TokenProvider;
import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@AllArgsConstructor
public class OAuthSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private static final String LOCAL_REDIRECT_URL = "http://localhost:3000";

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        log.info("auth succeeded");
        TokenProvider tokenProvider = new TokenProvider();
        String token = tokenProvider.create(authentication);

        Optional<Cookie> oCookie = Arrays.stream(request.getCookies()).filter(cookie -> cookie.getName().equals(REDIRECT_URI_PARAM)).findFirst();
        Optional<String> redirectUri = oCookie.map(Cookie::getValue);

        log.info("token {}", token);
        response.sendRedirect(redirectUri.orElseGet(() -> LOCAL_REDIRECT_URL) + "/oauthgoogle?token=" + token);
    }
}

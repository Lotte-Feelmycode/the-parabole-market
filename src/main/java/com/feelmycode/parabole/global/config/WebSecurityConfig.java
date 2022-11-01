package com.feelmycode.parabole.global.config;

import com.feelmycode.parabole.security.JwtAuthenticationFilter;
import com.feelmycode.parabole.security.OAuthSuccessHandler;
import com.feelmycode.parabole.security.OAuthUserServiceImpl;
import com.feelmycode.parabole.security.RedirectUrlCookieFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestRedirectFilter;
import org.springframework.security.web.authentication.Http403ForbiddenEntryPoint;
import org.springframework.web.filter.CorsFilter;

@EnableWebSecurity
@Slf4j
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Autowired
    private OAuthUserServiceImpl oAuthUserService; // 직접만든

    @Autowired
    private OAuthSuccessHandler oAuthSuccessHandler; // Success Handler 추가

    @Autowired
    private RedirectUrlCookieFilter redirectUrlFilter;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // http 시큐리티 빌더
        http.cors() // WebMvcConfig에서 이미 설정했으므로 기본 cors 설정.
            .and()
            .csrf().disable()
            .httpBasic().disable()
            .sessionManagement()  // session 기반이 아님을 선언
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

            .and()
            .authorizeRequests()
            .antMatchers("/","/api/v1/auth/**", "/oauth2/code/**", "/api/v1/product/**", "/api/v1/event/list").permitAll()
            .anyRequest()
            .authenticated()

            .and()
            .oauth2Login()
            .redirectionEndpoint()
            .baseUri("/oauth2/callback/**")

            .and()
            .authorizationEndpoint()
            .baseUri("/auth/authorize") // OAuth 2.0 흐름 시작을 위한 엔드포인트 추가

            .and()
            .userInfoEndpoint()
            .userService(oAuthUserService) // OAuthUserServiceImpl를 유저 서비스로 등록

            .and()
            .successHandler(oAuthSuccessHandler)

            .and()
            .exceptionHandling()
            .authenticationEntryPoint(new Http403ForbiddenEntryPoint()); // Http403ForbiddenEntryPoint 추가

        // CorsFilter -> jwtAuthFilter -> .... -> redirectUrlFilter -> OAuth2AuthorizationRequestRedirectFilter
        http.addFilterAfter(
            jwtAuthenticationFilter,
            CorsFilter.class
        );
        http.addFilterBefore(
            redirectUrlFilter,
            OAuth2AuthorizationRequestRedirectFilter.class
        );
    }
}

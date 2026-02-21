package com.hanselname.songaday.auth.config;

import com.hanselname.songaday.auth.filter.JWTFilter;
import com.hanselname.songaday.auth.utils.CookieUtils;
import com.hanselname.songaday.common.CommonUtils;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class FilterChainConfig {

    private final OAuthSuccessHandler successHandler;
    private final JWTFilter jwtFilter;
    private final CookieUtils cookieUtils;

    public FilterChainConfig(OAuthSuccessHandler successHandler, JWTFilter jwtFilter, CookieUtils cookieUtils) {
        this.successHandler = successHandler;
        this.jwtFilter = jwtFilter;
        this.cookieUtils = cookieUtils;
    }

    @Bean
    SecurityFilterChain filterChain(HttpSecurity httpSecurity) {
        httpSecurity.csrf(AbstractHttpConfigurer::disable).sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)).exceptionHandling(ex -> ex.authenticationEntryPoint((req, res, e) -> {
            res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        })).authorizeHttpRequests(auth -> auth.requestMatchers("/oauth2/**", "/error").permitAll().anyRequest().authenticated()).oauth2Login(oauth -> oauth.successHandler(successHandler)).addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class).logout(l -> l.logoutUrl(CommonUtils.API_PREFIX + "/logout").logoutSuccessHandler(((request, response, authentication) -> {
            response.addHeader(HttpHeaders.SET_COOKIE, cookieUtils.deleteAccessToken().toString());
            response.addHeader(HttpHeaders.SET_COOKIE, cookieUtils.deleteRefreshToken().toString());
        })));

        return httpSecurity.build();
    }
}

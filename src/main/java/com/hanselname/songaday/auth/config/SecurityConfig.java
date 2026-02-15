package com.hanselname.songaday.auth.config;

import com.hanselname.songaday.auth.filter.JWTFilter;
import com.hanselname.songaday.auth.utils.AuthUtils;
import com.hanselname.songaday.common.CommonUtils;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    private final OAuthSuccessHandler successHandler;
    private final JWTFilter jwtFilter;

    public SecurityConfig(OAuthSuccessHandler successHandler, JWTFilter jwtFilter) {
        this.successHandler = successHandler;
        this.jwtFilter = jwtFilter;
    }

    @Bean
    SecurityFilterChain filterChain(HttpSecurity httpSecurity) {
        httpSecurity.csrf(AbstractHttpConfigurer::disable).sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)).exceptionHandling(ex -> ex.authenticationEntryPoint((req, res, e) -> {
            res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        })).authorizeHttpRequests(auth -> auth.requestMatchers("/oauth2/**", "/error").permitAll().anyRequest().authenticated()).oauth2Login(oauth -> oauth.successHandler(successHandler)).addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class).logout(l -> l.logoutUrl(CommonUtils.API_PREFIX + "/logout").logoutSuccessHandler(((request, response, authentication) -> {
            Cookie cookie = new Cookie(AuthUtils.COOKIE_NAME, "");
            cookie.setMaxAge(0);
            cookie.setPath("/");
            response.addCookie(cookie);
        })));

        return httpSecurity.build();
    }
}

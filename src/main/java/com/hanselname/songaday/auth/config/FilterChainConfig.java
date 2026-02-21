package com.hanselname.songaday.auth.config;

import com.hanselname.songaday.auth.filter.JWTFilter;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class FilterChainConfig {

    private final OAuthSuccessHandler successHandler;
    private final JWTFilter jwtFilter;

    public FilterChainConfig(OAuthSuccessHandler successHandler, JWTFilter jwtFilter) {
        this.successHandler = successHandler;
        this.jwtFilter = jwtFilter;
    }

    @Bean
    SecurityFilterChain filterChain(HttpSecurity httpSecurity) {
        httpSecurity.csrf(AbstractHttpConfigurer::disable).sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)).cors(Customizer.withDefaults()).exceptionHandling(ex -> ex.authenticationEntryPoint((req, res, e) -> {
            res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        })).authorizeHttpRequests(auth -> auth.requestMatchers("/oauth2/**", "/error", "/api/auth/**").permitAll().anyRequest().authenticated()).oauth2Login(oauth -> oauth.successHandler(successHandler)).addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return httpSecurity.build();
    }
}

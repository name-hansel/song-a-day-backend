package com.hanselname.songaday.auth.filter;

import com.hanselname.songaday.auth.exception.AccessTokenExpiredException;
import com.hanselname.songaday.auth.service.JWTService;
import com.hanselname.songaday.auth.utils.CookieUtils;
import com.hanselname.songaday.user.entity.AppUserEntity;
import com.hanselname.songaday.user.repository.AppUserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Component
public class JWTFilter extends OncePerRequestFilter {
    private final JWTService jwtService;
    private final AppUserRepository userRepository;
    private final CookieUtils cookieUtils;

    public JWTFilter(JWTService jwtService, AppUserRepository userRepository, CookieUtils cookieUtils) {
        this.jwtService = jwtService;
        this.userRepository = userRepository;
        this.cookieUtils = cookieUtils;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String accessToken = cookieUtils.extractAccessToken(request);
        if (accessToken != null) {
            try {
                UUID appUserUuid = jwtService.validateAccessToken(accessToken);
                AppUserEntity appUserEntity = userRepository.findById(appUserUuid).orElse(null);

                if (appUserEntity != null) {
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(appUserEntity, null, List.of());
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            } catch (AccessTokenExpiredException exception) {
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
            }
        }

        filterChain.doFilter(request, response);
    }
}

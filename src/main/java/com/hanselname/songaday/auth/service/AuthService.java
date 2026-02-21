package com.hanselname.songaday.auth.service;

import com.hanselname.songaday.auth.exception.UserNotLoggedInException;
import com.hanselname.songaday.auth.utils.CookieUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AuthService {
    private final CookieUtils cookieUtils;
    private final RefreshTokenService refreshTokenService;

    public AuthService(CookieUtils cookieUtils, RefreshTokenService refreshTokenService) {
        this.cookieUtils = cookieUtils;
        this.refreshTokenService = refreshTokenService;
    }

    public void logout(HttpServletRequest request, HttpServletResponse response) {
        String refreshToken = cookieUtils.extractRefreshToken(request);
        refreshTokenService.deleteRefreshToken(refreshToken);

        response.addHeader(HttpHeaders.SET_COOKIE, cookieUtils.deleteAccessToken().toString());
        response.addHeader(HttpHeaders.SET_COOKIE, cookieUtils.deleteRefreshToken().toString());

        SecurityContextHolder.clearContext();
    }

    public UUID validateRefreshToken(HttpServletRequest request) {
        String oldRefreshToken = cookieUtils.extractRefreshToken(request);

        if (oldRefreshToken == null) {
            throw new UserNotLoggedInException();
        }

        UUID appUserUuid = refreshTokenService.validateRefreshToken(oldRefreshToken);
        refreshTokenService.deleteRefreshToken(oldRefreshToken);

        return appUserUuid;
    }
}

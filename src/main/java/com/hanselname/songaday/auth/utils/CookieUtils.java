package com.hanselname.songaday.auth.utils;

import com.hanselname.songaday.common.CommonUtils;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class CookieUtils {
    @Value("${app.jwt.access-token-expiration-seconds}")
    private long accessTokenExpirationSeconds;

    @Value("${app.jwt.refresh-token-expiration-seconds}")
    private long refreshTokenExpirationSeconds;

    private static final String ACCESS_TOKEN_NAME = "access_token";
    private static final String REFRESH_TOKEN_NAME = "refresh_token";
    private static final String ACCESS_TOKEN_PATH = CommonUtils.API_PREFIX;
    private static final String REFRESH_TOKEN_PATH = CommonUtils.AUTH_API_PREFIX;

    public ResponseCookie createAccessTokenCookie(String token) {
        return build(ACCESS_TOKEN_NAME, token, accessTokenExpirationSeconds, ACCESS_TOKEN_PATH, "Lax");
    }

    public ResponseCookie createRefreshTokenCookie(String token) {
        return build(REFRESH_TOKEN_NAME, token, refreshTokenExpirationSeconds, REFRESH_TOKEN_PATH, "Strict");
    }

    public String extractAccessToken(HttpServletRequest request) {
        return extract(request, ACCESS_TOKEN_NAME);
    }

    public String extractRefreshToken(HttpServletRequest request) {
        return extract(request, REFRESH_TOKEN_NAME);
    }

    public ResponseCookie deleteAccessToken() {
        return delete(ACCESS_TOKEN_NAME, ACCESS_TOKEN_PATH);
    }

    public ResponseCookie deleteRefreshToken() {
        return delete(REFRESH_TOKEN_NAME, REFRESH_TOKEN_PATH);
    }

    private ResponseCookie build(String name, String token, long secondsToExpiry, String path, String sameSite) {
        return ResponseCookie.from(name, token).httpOnly(true)
//                             .secure(true)
                             .path(path)
//                             .sameSite(sameSite)
                             .maxAge(secondsToExpiry).build();
    }

    private String extract(HttpServletRequest request, String name) {
        if (request.getCookies() == null) {
            return null;
        }

        return Arrays.stream(request.getCookies()).filter(cookie -> cookie.getName().equals(name)).map(Cookie::getValue).findFirst().orElse(null);
    }

    private ResponseCookie delete(String name, String path) {
        return ResponseCookie.from(name, "").httpOnly(true).secure(true).path(path).maxAge(0).build();
    }
}

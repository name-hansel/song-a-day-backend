package com.hanselname.songaday.auth.controller;

import com.hanselname.songaday.auth.service.JWTService;
import com.hanselname.songaday.auth.utils.CookieUtils;
import com.hanselname.songaday.common.CommonUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping(path = CommonUtils.AUTH_API_PREFIX)
public class AuthController {
    private final CookieUtils cookieUtils;
    private final JWTService jwtService;

    public AuthController(CookieUtils cookieUtils, JWTService jwtService) {
        this.cookieUtils = cookieUtils;
        this.jwtService = jwtService;
    }

    @PostMapping("/refresh")
    public ResponseEntity<Void> refreshAccessToken(HttpServletRequest request) {
        String refreshToken = cookieUtils.extractRefreshToken(request);
        UUID appUserUuid = jwtService.validateRefreshToken(refreshToken);

        String newAccessToken = jwtService.generateAccessToken(appUserUuid);
        String newRefreshToken = jwtService.generateRefreshToken(appUserUuid);

        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookieUtils.createAccessTokenCookie(newAccessToken).toString()).header(HttpHeaders.SET_COOKIE, cookieUtils.createRefreshTokenCookie(newRefreshToken).toString()).build();
    }
}

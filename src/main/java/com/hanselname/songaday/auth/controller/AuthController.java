package com.hanselname.songaday.auth.controller;

import com.hanselname.songaday.auth.service.AuthService;
import com.hanselname.songaday.auth.service.JWTService;
import com.hanselname.songaday.auth.service.RefreshTokenService;
import com.hanselname.songaday.auth.utils.CookieUtils;
import com.hanselname.songaday.common.CommonUtils;
import com.hanselname.songaday.user.dto.AuthAppUserResponseDTO;
import com.hanselname.songaday.user.service.AppUserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping(path = CommonUtils.AUTH_API_PREFIX)
public class AuthController {
    private final CookieUtils cookieUtils;
    private final JWTService jwtService;
    private final RefreshTokenService refreshTokenService;
    private final AuthService authService;
    private final AppUserService appUserService;

    public AuthController(CookieUtils cookieUtils, JWTService jwtService, RefreshTokenService refreshTokenService, AuthService authService, AppUserService appUserService) {
        this.cookieUtils = cookieUtils;
        this.jwtService = jwtService;
        this.refreshTokenService = refreshTokenService;
        this.authService = authService;
        this.appUserService = appUserService;
    }

    @PostMapping("/refresh-access-token")
    public ResponseEntity<Void> refreshAccessToken(HttpServletRequest request) {
        UUID appUserUuid = authService.validateRefreshToken(request);
        String newAccessToken = jwtService.generateAccessToken(appUserUuid);
        String newRefreshToken = refreshTokenService.generateRefreshTokenForAppUser(appUserUuid);

        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookieUtils.createAccessTokenCookie(newAccessToken).toString()).header(HttpHeaders.SET_COOKIE, cookieUtils.createRefreshTokenCookie(newRefreshToken).toString()).build();
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest request, HttpServletResponse response) {
        authService.logout(request, response);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/me")
    public AuthAppUserResponseDTO getAppUser(@AuthenticationPrincipal(expression = "uuid") UUID appUserUuid) {
        return appUserService.getAppUser(appUserUuid);
    }
}

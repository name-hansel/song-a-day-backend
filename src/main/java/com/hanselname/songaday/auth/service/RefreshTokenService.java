package com.hanselname.songaday.auth.service;

import com.hanselname.songaday.auth.entity.RefreshTokenEntity;
import com.hanselname.songaday.auth.repository.RefreshTokenRepository;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.UUID;

@Service
public class RefreshTokenService {
    private final JWTService jwtService;
    private final RefreshTokenRepository repository;

    public RefreshTokenService(JWTService jwtService, RefreshTokenRepository repository) {
        this.jwtService = jwtService;
        this.repository = repository;
    }

    public String generateRefreshTokenForUser(UUID appUserUuid) {
        String refreshToken = jwtService.generateRefreshToken(appUserUuid);
        String jti = jwtService.extractJtiFromRefreshToken(refreshToken);

        RefreshTokenEntity refreshTokenEntity = new RefreshTokenEntity();
        refreshTokenEntity.setUuid(UUID.fromString(jti));
        refreshTokenEntity.setAppUserUuid(appUserUuid);
        refreshTokenEntity.setTokenHash(hashToken(refreshToken));
        refreshTokenEntity.setExpiresAt(jwtService.extractExpiresAtFromRefreshToken(refreshToken));
        repository.save(refreshTokenEntity);

        return refreshToken;
    }

    private String hashToken(String token) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(token.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException exception) {
            throw new IllegalStateException("SHA-256 not available?", exception);
        }

    }
}

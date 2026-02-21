package com.hanselname.songaday.auth.service;

import com.hanselname.songaday.auth.entity.RefreshTokenEntity;
import com.hanselname.songaday.auth.repository.RefreshTokenRepository;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
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

    public String generateRefreshTokenForAppUser(UUID appUserUuid) {
        String refreshToken = jwtService.generateRefreshToken(appUserUuid);

        RefreshTokenEntity refreshTokenEntity = new RefreshTokenEntity();
        refreshTokenEntity.setUuid(jwtService.extractJtiFromRefreshToken(refreshToken));
        refreshTokenEntity.setAppUserUuid(appUserUuid);
        refreshTokenEntity.setTokenHash(hashToken(refreshToken));
        refreshTokenEntity.setExpiresAt(jwtService.extractExpiresAtFromRefreshToken(refreshToken));
        repository.save(refreshTokenEntity);

        return refreshToken;
    }

    public UUID validateRefreshToken(String token) {
        // TODO: Else, throw invalidtokenexception
        RefreshTokenEntity responseTokenEntity = repository.findById(jwtService.extractJtiFromRefreshToken(token)).orElseThrow(IllegalStateException::new);

        if (responseTokenEntity.getExpiresAt().isBefore(Instant.now())) {
            // TODO: Should logout the user
            throw new IllegalStateException("Refresh token expired");
        }

        if (!hashToken(token).equals(responseTokenEntity.getTokenHash())) {
            // TODO: Should logout the user
            throw new IllegalStateException("Refresh token invalid");
        }

        return responseTokenEntity.getAppUserUuid();
    }

    public void deleteRefreshToken(String oldToken) {
        repository.deleteById(jwtService.extractJtiFromRefreshToken(oldToken));
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

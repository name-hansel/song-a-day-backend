package com.hanselname.songaday.auth.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;

@Service
public class JWTService {

    @Value("${app.jwt.access-token-expiration-seconds}")
    private long accessTokenExpirationSeconds;

    @Value("${app.jwt.refresh-token-expiration-seconds}")
    private long refreshTokenExpirationSeconds;

    @Value("${app.jwt.access-token-secret}")
    private String accessTokenSecret;

    @Value("${app.jwt.refresh-token-secret}")
    private String refreshTokenSecret;

    private static final String JWT_TYPE = "type";
    private static final String ACCESS_TOKEN_TYPE = "access";
    private static final String REFRESH_TOKEN_TYPE = "refresh";

    private Key accessTokenKey() {
        return Keys.hmacShaKeyFor(accessTokenSecret.getBytes());
    }

    private Key refreshTokenKey() {
        return Keys.hmacShaKeyFor(refreshTokenSecret.getBytes());
    }

    public String generateAccessToken(UUID appUserUuid) {
        return Jwts.builder().setId(UUID.randomUUID().toString()).setSubject(appUserUuid.toString()).claim(JWT_TYPE, ACCESS_TOKEN_TYPE).setExpiration(Date.from(Instant.now().plusSeconds(accessTokenExpirationSeconds))).signWith(accessTokenKey(), SignatureAlgorithm.HS256).compact();
    }

    public String generateRefreshToken(UUID appUserUuid) {
        return Jwts.builder().setId(UUID.randomUUID().toString()).setSubject(appUserUuid.toString()).claim(JWT_TYPE, REFRESH_TOKEN_TYPE).setExpiration(Date.from(Instant.now().plusSeconds(refreshTokenExpirationSeconds))).signWith(refreshTokenKey(), SignatureAlgorithm.HS256).compact();
    }

    public UUID validateAccessToken(String token) {
        return UUID.fromString(Jwts.parserBuilder().setSigningKey(accessTokenKey()).build().parseClaimsJws(token).getBody().getSubject());
    }

    public UUID validateRefreshToken(String token) {
        return UUID.fromString(Jwts.parserBuilder().setSigningKey(refreshTokenKey()).build().parseClaimsJws(token).getBody().getSubject());
    }

    public String extractJtiFromRefreshToken(String token) {
        return Jwts.parserBuilder().setSigningKey(refreshTokenKey()).build().parseClaimsJws(token).getBody().getId();
    }

    public Instant extractExpiresAtFromRefreshToken(String token) {
        return Jwts.parserBuilder().setSigningKey(refreshTokenKey()).build().parseClaimsJws(token).getBody().getExpiration().toInstant();
    }
}

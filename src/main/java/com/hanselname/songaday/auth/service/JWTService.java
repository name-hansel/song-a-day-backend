package com.hanselname.songaday.auth.service;

import java.security.Key;
import java.util.Date;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Service
public class JWTService {

	@Value("${app.jwt.expiration}")
	private long expiration;

	@Value("${app.jwt.secret}")
	private String secret;

	private Key key() {
		return Keys.hmacShaKeyFor(secret.getBytes());
	}

	public String generate(UUID userId) {
		return Jwts.builder().setSubject(userId.toString()).setIssuedAt(new Date())
				.setExpiration(new Date(System.currentTimeMillis() + expiration))
				.signWith(key(), SignatureAlgorithm.HS256).compact();
	}

	public UUID validate(String token) {
		return UUID.fromString(
				Jwts.parserBuilder().setSigningKey(key()).build().parseClaimsJws(token).getBody().getSubject());
	}
}

package com.hanselname.songaday.auth.utils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;

import jakarta.annotation.Nonnull;

public class PkceUtil {
	private static final SecureRandom secureRandom = new SecureRandom();
	private static final Base64.Encoder base64UrlEncoder = Base64.getUrlEncoder().withoutPadding();

	private PkceUtil() {
	}

	public static String generateVerifier() {
		byte[] code = new byte[32];
		secureRandom.nextBytes(code);
		return base64UrlEncoder.encodeToString(code);
	}

	public static String generateChallenge(@Nonnull String verifier) {
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			byte[] hashed = digest.digest(verifier.getBytes(StandardCharsets.US_ASCII));

			return base64UrlEncoder.encodeToString(hashed);
		} catch (Exception e) {
			throw new IllegalStateException("Failed to generate PKCE challenge", e);
		}
	}
}

package com.hanselname.songaday.user.entity;

import java.time.Instant;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class AppUser {
	@Id
	@GeneratedValue
	private UUID id;

	private String spotifyId;
	private String displayName;
	private String email;

	@Column(length = 2000)
	private String accessToken;

	@Column(length = 2000)
	private String refreshToken;

	private Instant tokenExpiresAt;

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public String getSpotifyId() {
		return spotifyId;
	}

	public void setSpotifyId(String spotifyId) {
		this.spotifyId = spotifyId;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public String getRefreshToken() {
		return refreshToken;
	}

	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}

	public Instant getTokenExpiresAt() {
		return tokenExpiresAt;
	}

	public void setTokenExpiresAt(Instant tokenExpiresAt) {
		this.tokenExpiresAt = tokenExpiresAt;
	}

}

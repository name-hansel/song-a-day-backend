package com.hanselname.songaday.user.entity;

import com.hanselname.songaday.common.CommonUtils;
import com.hanselname.songaday.common.entity.AbstractEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

import java.time.Instant;
import java.util.UUID;

@Entity
public class AppUserEntity extends AbstractEntity {
    @Id
    @GeneratedValue
    private UUID uuid;

    private String spotifyId;

    private String spotifyDisplayName;

    private String spotifyEmail;

    @Column(length = 2000)
    private String spotifyAccessToken;

    @Column(length = 2000)
    private String spotifyRefreshToken;

    private Instant spotifyTokenExpiresAt;

    @Column(nullable = false)
    private String timezone = CommonUtils.DEFAULT_TIME_ZONE_ID;

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public String getSpotifyId() {
        return spotifyId;
    }

    public void setSpotifyId(String spotifyId) {
        this.spotifyId = spotifyId;
    }

    public String getSpotifyDisplayName() {
        return spotifyDisplayName;
    }

    public void setSpotifyDisplayName(String spotifyDisplayName) {
        this.spotifyDisplayName = spotifyDisplayName;
    }

    public String getSpotifyEmail() {
        return spotifyEmail;
    }

    public void setSpotifyEmail(String spotifyEmail) {
        this.spotifyEmail = spotifyEmail;
    }

    public String getSpotifyAccessToken() {
        return spotifyAccessToken;
    }

    public void setSpotifyAccessToken(String spotifyAccessToken) {
        this.spotifyAccessToken = spotifyAccessToken;
    }

    public String getSpotifyRefreshToken() {
        return spotifyRefreshToken;
    }

    public void setSpotifyRefreshToken(String spotifyRefreshToken) {
        this.spotifyRefreshToken = spotifyRefreshToken;
    }

    public Instant getSpotifyTokenExpiresAt() {
        return spotifyTokenExpiresAt;
    }

    public void setSpotifyTokenExpiresAt(Instant spotifyTokenExpiresAt) {
        this.spotifyTokenExpiresAt = spotifyTokenExpiresAt;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }
}

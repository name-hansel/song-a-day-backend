package com.hanselname.songaday.auth.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

import java.time.Instant;
import java.util.UUID;

@Entity
public class RefreshTokenEntity {
    @Id
    @GeneratedValue
    private UUID uuid;

    private UUID appUserUuid;

    private String tokenHash;

    private Instant expiresAt;

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public UUID getAppUserUuid() {
        return appUserUuid;
    }

    public void setAppUserUuid(UUID appUserUuid) {
        this.appUserUuid = appUserUuid;
    }

    public String getTokenHash() {
        return tokenHash;
    }

    public void setTokenHash(String tokenHash) {
        this.tokenHash = tokenHash;
    }

    public Instant getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(Instant expiresAt) {
        this.expiresAt = expiresAt;
    }
}

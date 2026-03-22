package com.hanselname.songaday.auth.repository;

import com.hanselname.songaday.auth.entity.RefreshTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import java.util.UUID;

public interface RefreshTokenRepository extends JpaRepository<RefreshTokenEntity, UUID> {

    @Modifying
    void deleteAllByAppUserUuid(UUID appUserUuid);
}

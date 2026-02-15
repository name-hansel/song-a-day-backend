package com.hanselname.songaday.user.repository;

import com.hanselname.songaday.user.entity.AppUserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface AppUserRepository extends JpaRepository<AppUserEntity, UUID> {
    Optional<AppUserEntity> findBySpotifyId(String spotifyId);
}

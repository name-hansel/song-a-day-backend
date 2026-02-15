package com.hanselname.songaday.song.repository;

import com.hanselname.songaday.song.entity.SongEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

public interface SongRepository extends JpaRepository<SongEntity, UUID> {
    Optional<SongEntity> findByAppUserUuidAndSongDate(UUID appUserUuid, LocalDate date);

    @Modifying
    void deleteByAppUserUuidAndSongDate(UUID appUserUuid, LocalDate date);
}

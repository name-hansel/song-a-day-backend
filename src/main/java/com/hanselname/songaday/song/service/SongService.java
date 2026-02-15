package com.hanselname.songaday.song.service;

import com.hanselname.songaday.song.dto.SongLogDTO;
import com.hanselname.songaday.song.entity.SongEntity;
import com.hanselname.songaday.song.repository.SongRepository;
import com.hanselname.songaday.user.entity.AppUserEntity;
import com.hanselname.songaday.user.repository.AppUserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

@Service
public class SongService {

    private final SongRepository songRepository;
    private final AppUserRepository appUserRepository;

    public SongService(SongRepository songRepository, AppUserRepository appUserRepository) {
        this.songRepository = songRepository;
        this.appUserRepository = appUserRepository;
    }

    // TODO: Return DTO
    public Optional<SongEntity> getSongOfDay(UUID appUserUuid, int day, int month, int year) {
        return songRepository.findByAppUserUuidAndSongDate(appUserUuid, LocalDate.of(year, month, day));
    }

    // TODO: Return DTO
    public Optional<SongEntity> logSongOfDay(UUID appUserUuid, SongLogDTO request) {
        AppUserEntity appUserEntity = appUserRepository.findById(appUserUuid).orElseThrow(() -> new RuntimeException("User not found."));
        Optional<SongEntity> existingSongOfDay = songRepository.findByAppUserUuidAndSongDate(appUserUuid, LocalDate.now());

        if (existingSongOfDay.isPresent()) {
            throw new RuntimeException("Already logged for the day");
        }

        // TODO: Duplicate check?
        // TODO: timezone handling
        SongEntity songEntity = new SongEntity();
        songEntity.setAppUser(appUserEntity);
        songEntity.setSpotifyId(request.spotifyId());
        songEntity.setSongDate(LocalDate.now());

        return Optional.of(songRepository.save(songEntity));
    }
}

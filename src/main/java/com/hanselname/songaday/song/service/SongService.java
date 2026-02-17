package com.hanselname.songaday.song.service;

import com.hanselname.songaday.song.dto.SongRequestDTO;
import com.hanselname.songaday.song.dto.SongResponseDTO;
import com.hanselname.songaday.song.entity.SongEntity;
import com.hanselname.songaday.song.mapper.SongMapper;
import com.hanselname.songaday.song.repository.SongRepository;
import com.hanselname.songaday.spotify.dto.TrackSearchDTO;
import com.hanselname.songaday.spotify.service.SpotifyService;
import com.hanselname.songaday.user.entity.AppUserEntity;
import com.hanselname.songaday.user.repository.AppUserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class SongService {

    private final SpotifyService spotifyService;

    private final SongRepository songRepository;
    private final AppUserRepository appUserRepository;

    private final SongMapper songMapper;

    public SongService(SpotifyService spotifyService, SongRepository songRepository, AppUserRepository appUserRepository, SongMapper songMapper) {
        this.spotifyService = spotifyService;
        this.songRepository = songRepository;
        this.appUserRepository = appUserRepository;
        this.songMapper = songMapper;
    }

    public SongResponseDTO getSongOfDay(UUID appUserUuid) {
        AppUserEntity appUserEntity = getAppUserEntityByUuid(appUserUuid);
        LocalDate today = getLocalDateForUser(appUserEntity);
        return getSongOfDay(appUserEntity.getUuid(), today.getDayOfMonth(), today.getMonthValue(), today.getYear());
    }

    public SongResponseDTO getSongOfDay(UUID appUserUuid, int day, int month, int year) {
        AppUserEntity appUserEntity = getAppUserEntityByUuid(appUserUuid);
        return songRepository.findByAppUserUuidAndSongDate(appUserUuid, LocalDate.of(year, month, day)).map(song -> getSongResponseDTO(appUserEntity, song)).orElse(null);
    }

    public SongResponseDTO logSongOfDay(UUID appUserUuid, SongRequestDTO request) {
        AppUserEntity appUserEntity = getAppUserEntityByUuid(appUserUuid);
        LocalDate today = getLocalDateForUser(appUserEntity);

        Optional<SongEntity> existingSongOfDay = songRepository.findByAppUserUuidAndSongDate(appUserEntity.getUuid(), today);

        if (existingSongOfDay.isPresent()) {
            throw new RuntimeException("Already logged for the day");
        }

        SongEntity songEntity = new SongEntity();
        songEntity.setAppUser(appUserEntity);
        songEntity.setSpotifyId(request.spotifyId());
        songEntity.setSongDate(today);

        return getSongResponseDTO(appUserEntity, songRepository.save(songEntity));
    }

    @Transactional
    public void deleteSongOfDay(UUID appUserUuid) {
        AppUserEntity appUserEntity = getAppUserEntityByUuid(appUserUuid);
        songRepository.deleteByAppUserUuidAndSongDate(appUserEntity.getUuid(), getLocalDateForUser(appUserEntity));
    }

    @Transactional
    public SongResponseDTO updateSongOfDay(UUID appUserUuid, SongRequestDTO request) {
        AppUserEntity appUserEntity = getAppUserEntityByUuid(appUserUuid);
        SongEntity songEntity = songRepository.findByAppUserUuidAndSongDate(appUserEntity.getUuid(), getLocalDateForUser(appUserEntity)).orElseThrow(() -> new RuntimeException("Song not logged"));
        songEntity.setSpotifyId(request.spotifyId());

        return getSongResponseDTO(appUserEntity, songEntity);
    }

    private SongResponseDTO getSongResponseDTO(AppUserEntity appUserEntity, SongEntity songEntity) {
        TrackSearchDTO track = spotifyService.getTrackBySpotifyId(appUserEntity, songEntity.getSpotifyId());

        if (track == null) {
            throw new RuntimeException("Song not found on Spotify.");
        }

        return songMapper.toDTO(songEntity, track);
    }

    private LocalDate getLocalDateForUser(AppUserEntity appUserEntity) {
        return ZonedDateTime.now(ZoneId.of(appUserEntity.getTimezone())).toLocalDate();
    }

    private AppUserEntity getAppUserEntityByUuid(UUID appUserUuid) {
        return appUserRepository.findById(appUserUuid).orElseThrow(() -> new RuntimeException("User not found."));
    }
}

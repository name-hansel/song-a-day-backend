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

import java.time.LocalDate;
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

    public SongResponseDTO getSongOfDay(AppUserEntity appUserEntity, int day, int month, int year) {
        Optional<SongEntity> songEntity = songRepository.findByAppUserUuidAndSongDate(appUserEntity.getUuid(), LocalDate.of(year, month, day));

        return songEntity.map(song -> getSongResponseDTO(appUserEntity, song)).orElse(null);
    }

    public SongResponseDTO logSongOfDay(UUID appUserUuid, SongRequestDTO request) {
        AppUserEntity appUserEntity = appUserRepository.findById(appUserUuid).orElseThrow(() -> new RuntimeException("User not found."));
        Optional<SongEntity> existingSongOfDay = songRepository.findByAppUserUuidAndSongDate(appUserUuid, LocalDate.now());

        if (existingSongOfDay.isPresent()) {
            throw new RuntimeException("Already logged for the day");
        }

        // TODO: timezone handling
        SongEntity songEntity = new SongEntity();
        songEntity.setAppUser(appUserEntity);
        songEntity.setSpotifyId(request.spotifyId());
        songEntity.setSongDate(LocalDate.now());

        return getSongResponseDTO(appUserEntity, songEntity);
    }

    private SongResponseDTO getSongResponseDTO(AppUserEntity appUserEntity, SongEntity songEntity) {
        TrackSearchDTO track = spotifyService.getTrackBySpotifyId(appUserEntity, songEntity.getSpotifyId());

        if (track == null) {
            throw new RuntimeException("Song not found on Spotify.");
        }

        return songMapper.toDTO(songEntity, track);
    }
}

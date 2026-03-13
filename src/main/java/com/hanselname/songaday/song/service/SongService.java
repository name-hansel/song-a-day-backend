package com.hanselname.songaday.song.service;

import com.hanselname.songaday.common.exception.exception.InvalidDataException;
import com.hanselname.songaday.common.utils.StringUtils;
import com.hanselname.songaday.song.dto.SongRequestDTO;
import com.hanselname.songaday.song.dto.SongResponseDTO;
import com.hanselname.songaday.song.dto.UpdateMemoryRequestDTO;
import com.hanselname.songaday.song.entity.SongEntity;
import com.hanselname.songaday.song.exception.InvalidDateException;
import com.hanselname.songaday.song.exception.SongNotFoundException;
import com.hanselname.songaday.song.mapper.SongMapper;
import com.hanselname.songaday.song.repository.SongRepository;
import com.hanselname.songaday.spotify.dto.TrackSearchDTO;
import com.hanselname.songaday.spotify.service.SpotifyService;
import com.hanselname.songaday.user.entity.AppUserEntity;
import com.hanselname.songaday.user.exception.UserNotFoundException;
import com.hanselname.songaday.user.repository.AppUserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.hanselname.songaday.user.AppUserUtils.getLocalDateForAppUser;

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
        LocalDate today = getLocalDateForAppUser(appUserEntity);
        return getSongOfDay(appUserEntity.getUuid(), today.toString());
    }

    public SongResponseDTO getSongOfDay(UUID appUserUuid, String date) {
        try {
            String[] dateComponents = date.split("-");

            if (dateComponents.length != 3) {
                throw new InvalidDateException();
            }

            int year = Integer.parseInt(dateComponents[0]);
            int month = Integer.parseInt(dateComponents[1]);
            int day = Integer.parseInt(dateComponents[2]);

            AppUserEntity appUserEntity = getAppUserEntityByUuid(appUserUuid);
            return songRepository.findByAppUserUuidAndSongDate(appUserUuid,
                                         LocalDate.of(year, month, day))
                                 .map(song -> getSongResponseDTO(appUserEntity,
                                         song)).orElse(null);
        } catch (DateTimeException exc) {
            throw new InvalidDateException();
        }
    }

    public SongResponseDTO logSongOfDay(UUID appUserUuid, SongRequestDTO request) {
        AppUserEntity appUserEntity = getAppUserEntityByUuid(appUserUuid);
        LocalDate today = getLocalDateForAppUser(appUserEntity);

        SongEntity songEntity = songRepository
                .findByAppUserUuidAndSongDate(appUserEntity.getUuid(), today)
                .orElseGet(() -> {
                    SongEntity newSongEntity = new SongEntity();
                    newSongEntity.setAppUser(appUserEntity);
                    newSongEntity.setSongDate(today);

                    return newSongEntity;
                });

        String trimmedMemory = StringUtils.trim(request.memory());
        if (trimmedMemory != null && trimmedMemory.length() > 160) {
            throw new InvalidDataException("MEMORY");
        }

        songEntity.setMemory(request.memory());
        songEntity.setSpotifyId(request.spotifyId());

        return getSongResponseDTO(appUserEntity,
                songRepository.save(songEntity));
    }

    @Transactional
    public void deleteSongOfDay(UUID appUserUuid) {
        AppUserEntity appUserEntity = getAppUserEntityByUuid(appUserUuid);
        songRepository.deleteByAppUserUuidAndSongDate(appUserEntity.getUuid(),
                getLocalDateForAppUser(appUserEntity));
    }

    public List<SongResponseDTO> getSongHistoryForLastWeek(UUID appUserUuid) {
        AppUserEntity appUserEntity = getAppUserEntityByUuid(appUserUuid);
        LocalDate today = getLocalDateForAppUser(appUserEntity);
        LocalDate startDate = today.minusDays(6);

        Map<LocalDate, SongEntity> songEntityMap = songRepository
                .findByAppUserUuidAndSongDateBetweenOrderBySongDateDesc(
                        appUserUuid, startDate, today).stream().collect(
                        Collectors.toMap(SongEntity::getSongDate,
                                Function.identity()));

        List<SongResponseDTO> result = new ArrayList<>();

        for (int i = 6; i >= 0; i--) {
            LocalDate date = today.minusDays(i);
            SongEntity songEntity = songEntityMap.get(date);
            result.add(songEntity != null ? getSongResponseDTO(appUserEntity,
                    songEntity) : null);
        }

        return result;
    }

    public SongResponseDTO updateMemoryForSong(UUID appUserUuid, UUID songUuid, UpdateMemoryRequestDTO request) {
        AppUserEntity appUserEntity = getAppUserEntityByUuid(appUserUuid);
        SongEntity songEntity = songRepository
                .findByAppUserUuidAndUuid(appUserUuid, songUuid)
                .orElseThrow(SongNotFoundException::new);

        String trimmedMemory = StringUtils.trim(request.updatedMemory());
        if (trimmedMemory != null && trimmedMemory.length() > 160) {
            throw new InvalidDataException("MEMORY");
        }
        songEntity.setMemory(request.updatedMemory());

        return getSongResponseDTO(appUserEntity,
                songRepository.save(songEntity));
    }

    private SongResponseDTO getSongResponseDTO(AppUserEntity appUserEntity, SongEntity songEntity) {
        TrackSearchDTO track = spotifyService.getTrackBySpotifyId(appUserEntity,
                songEntity.getSpotifyId());

        if (track == null) {
            throw new RuntimeException("Song not found on Spotify.");
        }

        return songMapper.toDTO(songEntity, track,
                ZoneId.of(appUserEntity.getTimezone()));
    }

    private AppUserEntity getAppUserEntityByUuid(UUID appUserUuid) {
        return appUserRepository.findById(appUserUuid)
                                .orElseThrow(UserNotFoundException::new);
    }
}

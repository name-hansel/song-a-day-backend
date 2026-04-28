package com.hanselname.songaday.song.service;

import com.hanselname.songaday.common.exception.exception.ActionNotAllowedException;
import com.hanselname.songaday.common.exception.exception.InvalidDataException;
import com.hanselname.songaday.common.utils.StringUtils;
import com.hanselname.songaday.song.dto.SongHistoryResponse;
import com.hanselname.songaday.song.dto.SongRequestDTO;
import com.hanselname.songaday.song.dto.SongResponseDTO;
import com.hanselname.songaday.song.dto.UpdateMemoryRequestDTO;
import com.hanselname.songaday.song.entity.SongEntity;
import com.hanselname.songaday.song.exception.InvalidDateException;
import com.hanselname.songaday.song.exception.SongNotFoundException;
import com.hanselname.songaday.song.mapper.SongMapper;
import com.hanselname.songaday.song.repository.SongRepository;
import com.hanselname.songaday.song.utils.SongUtils;
import com.hanselname.songaday.spotify.dto.TrackSearchDTO;
import com.hanselname.songaday.spotify.service.SpotifyService;
import com.hanselname.songaday.user.entity.AppUserEntity;
import com.hanselname.songaday.user.exception.UserNotFoundException;
import com.hanselname.songaday.user.repository.AppUserRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
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
            return songRepository.findByAppUserUuidAndSongDate(appUserUuid, LocalDate.of(year, month, day))
                    .map(song -> getSongResponseDTO(appUserEntity, song)).orElse(null);
        } catch (DateTimeException exc) {
            throw new InvalidDateException();
        }
    }

    @Transactional
    public SongResponseDTO logSongOfDay(UUID appUserUuid, SongRequestDTO request) {
        String trimmedMemory = StringUtils.trim(request.memory());
        if (SongUtils.isSongMemoryInvalid(trimmedMemory)) {
            throw new InvalidDataException("MEMORY");
        }

        AppUserEntity appUserEntity = getAppUserEntityByUuid(appUserUuid);
        LocalDate today = getLocalDateForAppUser(appUserEntity);

        SongEntity songEntity = songRepository.findByAppUserUuidAndSongDate(appUserUuid, today).orElseGet(() -> {
            SongEntity newSongEntity = new SongEntity();
            newSongEntity.setAppUser(appUserEntity);
            newSongEntity.setSongDate(today);

            return newSongEntity;
        });

        songEntity.setMemory(trimmedMemory);
        songEntity.setSpotifyId(request.spotifyId());

        return getSongResponseDTO(appUserEntity, songRepository.save(songEntity));
    }

    @Transactional
    public void deleteSongOfDay(UUID appUserUuid) {
        AppUserEntity appUserEntity = getAppUserEntityByUuid(appUserUuid);
        songRepository.deleteByAppUserUuidAndSongDate(appUserEntity.getUuid(), getLocalDateForAppUser(appUserEntity));
    }

    public List<SongResponseDTO> getSongHistoryForLastWeek(UUID appUserUuid) {
        AppUserEntity appUserEntity = getAppUserEntityByUuid(appUserUuid);
        LocalDate today = getLocalDateForAppUser(appUserEntity);
        LocalDate startDate = today.minusDays(6);

        Map<LocalDate, SongEntity> songEntityMap = songRepository.findByAppUserUuidAndSongDateBetweenOrderBySongDateDesc(
                        appUserUuid, startDate, today).stream()
                .collect(Collectors.toMap(SongEntity::getSongDate, Function.identity()));

        List<SongResponseDTO> result = new ArrayList<>();

        for (int i = 6; i >= 0; i--) {
            LocalDate date = today.minusDays(i);
            SongEntity songEntity = songEntityMap.get(date);
            result.add(songEntity != null ? getSongResponseDTO(appUserEntity, songEntity) : null);
        }

        return result;
    }

    public SongResponseDTO updateMemoryForSong(UUID appUserUuid, UUID songUuid, UpdateMemoryRequestDTO request) {
        AppUserEntity appUserEntity = getAppUserEntityByUuid(appUserUuid);
        SongEntity songEntity = songRepository.findByAppUserUuidAndUuid(appUserUuid, songUuid)
                .orElseThrow(SongNotFoundException::new);

        LocalDate today = getLocalDateForAppUser(appUserEntity);
        if (!today.equals(songEntity.getSongDate())) {
            throw new ActionNotAllowedException();
        }

        String trimmedMemory = StringUtils.trim(request.updatedMemory());
        if (SongUtils.isSongMemoryInvalid(trimmedMemory)) {
            throw new InvalidDataException("MEMORY");
        }

        songEntity.setMemory(trimmedMemory);

        return getSongResponseDTO(appUserEntity, songRepository.save(songEntity));
    }

    public boolean hasLoggedSongForToday(AppUserEntity appUserEntity) {
        return songRepository.findByAppUserUuidAndSongDate(appUserEntity.getUuid(),
                getLocalDateForAppUser(appUserEntity)).isPresent();
    }

    public void deleteSongs(UUID appUserUuid) {
        songRepository.deleteAllByAppUserUuid(appUserUuid);
    }

    public SongHistoryResponse getSongHistory(UUID appUserUuid, LocalDate beforeDate, LocalDate afterDate, int limit) {
        if (beforeDate != null && afterDate != null) {
            throw new ActionNotAllowedException();
        }

        AppUserEntity appUserEntity = getAppUserEntityByUuid(appUserUuid);

        // Next
        if (beforeDate != null) {
            return getOlder(appUserEntity, beforeDate, limit);
        }

        // Previous
        if (afterDate != null) {
            return getNewer(appUserEntity, afterDate, limit);
        }

        return getLatest(appUserEntity, limit);
    }

    private SongHistoryResponse getLatest(AppUserEntity appUserEntity, int limit) {
        PageRequest pageRequest = PageRequest.of(0, limit + 1);
        List<SongEntity> songEntities = songRepository.findByAppUserUuidOrderBySongDateDesc(appUserEntity.getUuid(),
                pageRequest);
        boolean hasMoreNext = checkIfHasMoreAndPrune(songEntities, limit);

        return new SongHistoryResponse(getSongResponseDTOs(songEntities, appUserEntity), getNextDate(songEntities),
                null, hasMoreNext, false);
    }

    private SongHistoryResponse getOlder(AppUserEntity appUserEntity, LocalDate beforeDate, int limit) {
        PageRequest pageRequest = PageRequest.of(0, limit + 1);
        List<SongEntity> songEntities = songRepository.findByAppUserUuidAndSongDateLessThanOrderBySongDateDesc(
                appUserEntity.getUuid(), beforeDate, pageRequest);

        boolean hasMoreNext = checkIfHasMoreAndPrune(songEntities, limit);

        return new SongHistoryResponse(getSongResponseDTOs(songEntities, appUserEntity), getNextDate(songEntities),
                getPreviousDate(songEntities), hasMoreNext, true);
    }

    private SongHistoryResponse getNewer(AppUserEntity appUserEntity, LocalDate afterDate, int limit) {
        PageRequest pageRequest = PageRequest.of(0, limit + 1);
        List<SongEntity> songEntities = songRepository.findByAppUserUuidAndSongDateGreaterThanOrderBySongDateAsc(
                appUserEntity.getUuid(), afterDate, pageRequest);
        
        Collections.reverse(songEntities);
        boolean hasMorePrevious = checkIfHasMoreAndPrune(songEntities, limit);

        return new SongHistoryResponse(getSongResponseDTOs(songEntities, appUserEntity), getNextDate(songEntities),
                getPreviousDate(songEntities), true, hasMorePrevious);
    }

    private List<SongResponseDTO> getSongResponseDTOs(Collection<SongEntity> songEntities, AppUserEntity appUserEntity) {
        return songEntities.stream().map(song -> getSongResponseDTO(appUserEntity, song)).toList();
    }

    private LocalDate getNextDate(List<SongEntity> songEntities) {
        return songEntities.isEmpty() ? null : songEntities.getLast().getSongDate();
    }

    private LocalDate getPreviousDate(List<SongEntity> songEntities) {
        return songEntities.isEmpty() ? null : songEntities.getFirst().getSongDate();
    }

    private boolean checkIfHasMoreAndPrune(List<SongEntity> songEntities, int limit) {
        boolean hasMoreNext = songEntities.size() > limit;
        if (hasMoreNext) {
            songEntities.removeLast();
        }

        return hasMoreNext;
    }

    private SongResponseDTO getSongResponseDTO(AppUserEntity appUserEntity, SongEntity songEntity) {
        TrackSearchDTO track = spotifyService.getTrackBySpotifyId(appUserEntity, songEntity.getSpotifyId());

        if (track == null) {
            throw new RuntimeException("Song not found on Spotify.");
        }

        return songMapper.toDTO(songEntity, track, ZoneId.of(appUserEntity.getTimezone()));
    }

    private AppUserEntity getAppUserEntityByUuid(UUID appUserUuid) {
        return appUserRepository.findById(appUserUuid).orElseThrow(UserNotFoundException::new);
    }
}

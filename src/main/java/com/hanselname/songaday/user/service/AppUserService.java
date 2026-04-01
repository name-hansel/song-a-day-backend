package com.hanselname.songaday.user.service;

import com.hanselname.songaday.auth.service.RefreshTokenService;
import com.hanselname.songaday.song.service.SongService;
import com.hanselname.songaday.user.dto.AuthAppUserResponseDTO;
import com.hanselname.songaday.user.dto.TimezoneResponseDTO;
import com.hanselname.songaday.user.entity.AppUserEntity;
import com.hanselname.songaday.user.exception.UserNotFoundException;
import com.hanselname.songaday.user.mapper.AppUserMapper;
import com.hanselname.songaday.user.repository.AppUserRepository;
import jakarta.annotation.Nonnull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Service
public class AppUserService {

    private final AppUserRepository repository;
    private final AppUserMapper appUserMapper;
    private final SongService songService;
    private final RefreshTokenService refreshTokenService;

    private static final List<String> SUPPORTED_TIMEZONES = List.of("UTC",
            "Europe/London", "Europe/Berlin", "Europe/Paris", "Europe/Moscow",
            "Asia/Dubai", "Asia/Kolkata", "Asia/Singapore", "Asia/Tokyo",
            "Asia/Shanghai", "Australia/Sydney", "America/New_York",
            "America/Chicago", "America/Denver", "America/Los_Angeles",
            "America/Sao_Paulo", "Africa/Johannesburg");

    private static final List<TimezoneResponseDTO> TIMEZONE_RESPONSE_DTOS;

    static {
        List<TimezoneResponseDTO> list = new ArrayList<>();

        for (String zone : SUPPORTED_TIMEZONES) {
            list.add(getTimezoneResponseDTO(zone));
        }

        TIMEZONE_RESPONSE_DTOS = Collections.unmodifiableList(list);
    }

    public AppUserService(AppUserRepository repository, AppUserMapper appUserMapper, SongService songService, RefreshTokenService refreshTokenService) {
        this.repository = repository;
        this.appUserMapper = appUserMapper;
        this.songService = songService;
        this.refreshTokenService = refreshTokenService;
    }

    @Transactional
    public AuthAppUserResponseDTO updateUserTimezone(UUID appUserUuid, String newTimezone) {
        AppUserEntity appUserEntity = repository.findById(appUserUuid)
                .orElseThrow(
                        UserNotFoundException::new);

        if (!ZoneId.getAvailableZoneIds().contains(newTimezone)) {
            throw new RuntimeException("Invalid timezone");
        }

        appUserEntity.setTimezone(newTimezone);
        return appUserMapper.toDTO(appUserEntity, songService.hasLoggedSongForToday(appUserEntity));
    }

    public AuthAppUserResponseDTO getAppUser(UUID appUserUuid) {
        AppUserEntity appUserEntity = repository.findById(appUserUuid)
                .orElseThrow(UserNotFoundException::new);
        return appUserMapper.toDTO(appUserEntity,
                songService.hasLoggedSongForToday(appUserEntity));
    }

    public List<TimezoneResponseDTO> getAllTimezones() {
        return TIMEZONE_RESPONSE_DTOS;
    }

    @Transactional
    public void deleteAppUser(@Nonnull UUID appUserUuid) {
        if (repository.findById(appUserUuid).isEmpty()) {
            throw new UserNotFoundException();
        }

        refreshTokenService.deleteRefreshTokens(appUserUuid);
        songService.deleteSongs(appUserUuid);
        repository.deleteById(appUserUuid);
    }

    private static TimezoneResponseDTO getTimezoneResponseDTO(String zone) {
        ZoneId zoneId = ZoneId.of(zone);
        ZoneOffset offset = ZonedDateTime.now(zoneId).getOffset();

        String label = "(UTC" + offset + ") " + zone;
        return new TimezoneResponseDTO(label, zone);
    }
}

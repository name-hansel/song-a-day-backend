package com.hanselname.songaday.user.service;

import com.hanselname.songaday.user.dto.AuthAppUserResponseDTO;
import com.hanselname.songaday.user.dto.TimezoneResponseDTO;
import com.hanselname.songaday.user.entity.AppUserEntity;
import com.hanselname.songaday.user.exception.UserNotFoundException;
import com.hanselname.songaday.user.mapper.AppUserMapper;
import com.hanselname.songaday.user.repository.AppUserRepository;
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

    private final AppUserRepository appUserRepository;
    private final AppUserMapper appUserMapper;

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

    public AppUserService(AppUserRepository appUserRepository, AppUserMapper appUserMapper) {
        this.appUserRepository = appUserRepository;
        this.appUserMapper = appUserMapper;
    }

    @Transactional
    public TimezoneResponseDTO updateUserTimezone(UUID appUserUuid, String newTimezone) {
        AppUserEntity appUserEntity = appUserRepository.findById(appUserUuid)
                                                       .orElseThrow(
                                                               UserNotFoundException::new);

        if (!ZoneId.getAvailableZoneIds().contains(newTimezone)) {
            throw new RuntimeException("Invalid timezone");
        }

        appUserEntity.setTimezone(newTimezone);
        return getTimezoneResponseDTO(newTimezone);
    }

    public AuthAppUserResponseDTO getAppUser(UUID appUserUuid) {
        AppUserEntity appUserEntity = appUserRepository.findById(appUserUuid)
                                                       .orElseThrow(
                                                               UserNotFoundException::new);
        return appUserMapper.toDTO(appUserEntity);
    }

    public List<TimezoneResponseDTO> getAllTimezones() {
        return TIMEZONE_RESPONSE_DTOS;
    }

    private static TimezoneResponseDTO getTimezoneResponseDTO(String zone) {
        ZoneId zoneId = ZoneId.of(zone);
        ZoneOffset offset = ZonedDateTime.now(zoneId).getOffset();

        String label = "(UTC" + offset + ") " + zone;
        return new TimezoneResponseDTO(label, zone);
    }
}

package com.hanselname.songaday.user.service;

import com.hanselname.songaday.user.dto.AppUserTimezoneResponseDTO;
import com.hanselname.songaday.user.dto.AuthAppUserResponseDTO;
import com.hanselname.songaday.user.entity.AppUserEntity;
import com.hanselname.songaday.user.mapper.AppUserMapper;
import com.hanselname.songaday.user.repository.AppUserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZoneId;
import java.util.UUID;

@Service
public class AppUserService {

    private final AppUserRepository appUserRepository;
    private final AppUserMapper appUserMapper;

    public AppUserService(AppUserRepository appUserRepository, AppUserMapper appUserMapper) {
        this.appUserRepository = appUserRepository;
        this.appUserMapper = appUserMapper;
    }

    @Transactional
    public AppUserTimezoneResponseDTO updateUserTimezone(UUID appUserUuid, String newTimezone) {
        AppUserEntity appUserEntity = appUserRepository.findById(appUserUuid).orElseThrow(() -> new RuntimeException("User not found."));

        if (!ZoneId.getAvailableZoneIds().contains(newTimezone)) {
            throw new RuntimeException("Invalid timezone");
        }

        appUserEntity.setTimezone(newTimezone);
        return new AppUserTimezoneResponseDTO(newTimezone);
    }

    public AuthAppUserResponseDTO getAppUser(UUID appUserUuid) {
        AppUserEntity appUserEntity = appUserRepository.findById(appUserUuid).orElseThrow(() -> new RuntimeException("User not found."));
        return appUserMapper.toDTO(appUserEntity);
    }
}

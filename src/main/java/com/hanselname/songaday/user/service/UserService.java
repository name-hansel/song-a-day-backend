package com.hanselname.songaday.user.service;

import com.hanselname.songaday.user.dto.UserTimezoneResponseDTO;
import com.hanselname.songaday.user.entity.AppUserEntity;
import com.hanselname.songaday.user.repository.AppUserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZoneId;
import java.util.UUID;

@Service
public class UserService {

    private final AppUserRepository appUserRepository;

    public UserService(AppUserRepository appUserRepository) {
        this.appUserRepository = appUserRepository;
    }

    @Transactional
    public UserTimezoneResponseDTO updateUserTimezone(UUID appUserUuid, String newTimezone) {
        AppUserEntity appUserEntity = appUserRepository.findById(appUserUuid).orElseThrow(() -> new RuntimeException("User not found."));

        if (!ZoneId.getAvailableZoneIds().contains(newTimezone)) {
            throw new RuntimeException("Invalid timezone");
        }

        appUserEntity.setTimezone(newTimezone);
        return new UserTimezoneResponseDTO(newTimezone);
    }
}

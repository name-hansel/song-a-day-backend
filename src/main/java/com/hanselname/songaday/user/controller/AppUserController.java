package com.hanselname.songaday.user.controller;

import com.hanselname.songaday.common.utils.CommonUtils;
import com.hanselname.songaday.user.dto.AppUserTimezoneRequestDTO;
import com.hanselname.songaday.user.dto.AuthAppUserResponseDTO;
import com.hanselname.songaday.user.dto.TimezoneResponseDTO;
import com.hanselname.songaday.user.service.AppUserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(path = CommonUtils.USER_API_PREFIX)
public class AppUserController {

    private final AppUserService appUserService;

    public AppUserController(AppUserService appUserService) {
        this.appUserService = appUserService;
    }

    @GetMapping("/me")
    public AuthAppUserResponseDTO getAppUser(@AuthenticationPrincipal(expression = "uuid") UUID appUserUuid) {
        return appUserService.getAppUser(appUserUuid);
    }

    @GetMapping("/timezones")
    public List<TimezoneResponseDTO> getAllTimezones() {
        return appUserService.getAllTimezones();
    }

    @PatchMapping("/timezone")
    public TimezoneResponseDTO updateUserTimezone(@AuthenticationPrincipal(expression = "uuid") UUID appUserUuid, @RequestBody AppUserTimezoneRequestDTO request) {
        return appUserService.updateUserTimezone(appUserUuid,
                request.timezone());
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteAppUser(@AuthenticationPrincipal(expression = "uuid") UUID appUserUuid) {
        appUserService.deleteAppUser(appUserUuid);
        return ResponseEntity.noContent().build();
    }
}

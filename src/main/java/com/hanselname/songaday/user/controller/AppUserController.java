package com.hanselname.songaday.user.controller;

import com.hanselname.songaday.common.CommonUtils;
import com.hanselname.songaday.user.dto.AppUserTimezoneRequestDTO;
import com.hanselname.songaday.user.dto.AppUserTimezoneResponseDTO;
import com.hanselname.songaday.user.service.AppUserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping(path = CommonUtils.USER_API_PREFIX)
public class AppUserController {

    private final AppUserService appUserService;

    public AppUserController(AppUserService appUserService) {
        this.appUserService = appUserService;
    }

    @PatchMapping("/time-zone")
    public AppUserTimezoneResponseDTO updateUserTimezone(@AuthenticationPrincipal(expression = "uuid") UUID appUserUuid, @RequestBody AppUserTimezoneRequestDTO request) {
        return appUserService.updateUserTimezone(appUserUuid, request.timezone());
    }
}

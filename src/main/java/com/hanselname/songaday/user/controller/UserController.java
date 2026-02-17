package com.hanselname.songaday.user.controller;

import com.hanselname.songaday.common.CommonUtils;
import com.hanselname.songaday.user.dto.UserTimezoneRequestDTO;
import com.hanselname.songaday.user.dto.UserTimezoneResponseDTO;
import com.hanselname.songaday.user.service.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping(path = CommonUtils.AUTH_USER_API_PREFIX)
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PatchMapping("/time-zone")
    public UserTimezoneResponseDTO updateUserTimezone(@AuthenticationPrincipal(expression = "uuid") UUID appUserUuid, @RequestBody UserTimezoneRequestDTO request) {
        return userService.updateUserTimezone(appUserUuid, request.timezone());
    }
}

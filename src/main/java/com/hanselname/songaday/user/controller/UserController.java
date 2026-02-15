package com.hanselname.songaday.user.controller;

import com.hanselname.songaday.spotify.util.SpotifyUtils;
import com.hanselname.songaday.user.entity.AppUserEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/app-user")
public class UserController {

    @GetMapping("/me")
    public Map<String, Object> me(Authentication auth) {
        AppUserEntity user = (AppUserEntity) auth.getPrincipal();

        return Map.of(SpotifyUtils.SPOTIFY_ID_ATTRIBUTE_NAME, user.getSpotifyId(), SpotifyUtils.SPOTIFY_DISPLAY_NAME_ATTRIBUTE_NAME, user.getSpotifyDisplayName(), SpotifyUtils.SPOTIFY_EMAIL_ATTRIBUTE_NAME, user.getSpotifyEmail());
    }
}

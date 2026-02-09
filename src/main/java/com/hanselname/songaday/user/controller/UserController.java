package com.hanselname.songaday.user.controller;

import java.util.Map;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hanselname.songaday.spotify.util.SpotifyUtils;
import com.hanselname.songaday.user.entity.AppUser;

@RestController
@RequestMapping("/v1/api")
public class UserController {

	@GetMapping("/me")
	public Map<String, Object> me(Authentication auth) {
		AppUser user = (AppUser) auth.getPrincipal();

		return Map.of(SpotifyUtils.SPOTIFY_ID_ATTRIBUTE_NAME, user.getSpotifyId(),
				SpotifyUtils.SPOTIFY_DISPLAY_NAME_ATTRIBUTE_NAME, user.getSpotifyDisplayName(),
				SpotifyUtils.SPOTIFY_EMAIL_ATTRIBUTE_NAME, user.getSpotifyEmail());
	}
}

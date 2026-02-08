package com.hanselname.songaday.spotify.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hanselname.songaday.spotify.service.SpotifyService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/v1/api/spotify")
@RequiredArgsConstructor
public class SpotifyController {
	private final SpotifyService spotifyService;

	@GetMapping("/me/player")
	public ResponseEntity<?> currentTrack(Authentication authentication) {
		return ResponseEntity.ok(spotifyService.getCurrentTrack(authentication));
	}
}

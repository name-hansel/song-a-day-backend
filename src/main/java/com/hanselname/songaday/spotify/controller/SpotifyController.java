package com.hanselname.songaday.spotify.controller;

import com.hanselname.songaday.spotify.service.SpotifyService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/spotify")
public class SpotifyController {
    private final SpotifyService spotifyService;

    public SpotifyController(SpotifyService spotifyService) {
        this.spotifyService = spotifyService;
    }

    @GetMapping("/search-track")
    public ResponseEntity<?> searchForTrack(Authentication authentication, @RequestParam(name = "q") String searchQuery) {
        return ResponseEntity.ok(spotifyService.searchForTrack(authentication, searchQuery));
    }
}

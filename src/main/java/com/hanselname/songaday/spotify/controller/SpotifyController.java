package com.hanselname.songaday.spotify.controller;

import com.hanselname.songaday.spotify.dto.TrackSearchDTO;
import com.hanselname.songaday.spotify.service.SpotifyService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/spotify")
public class SpotifyController {
    private final SpotifyService spotifyService;

    public SpotifyController(SpotifyService spotifyService) {
        this.spotifyService = spotifyService;
    }

    @GetMapping("/search-track")
    public List<TrackSearchDTO> searchForTrack(@AuthenticationPrincipal(expression = "uuid") UUID appUserUuid, @RequestParam(name = "q") String searchQuery) {
        return spotifyService.searchForTrack(appUserUuid, searchQuery);
    }
}

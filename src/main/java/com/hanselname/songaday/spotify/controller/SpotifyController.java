package com.hanselname.songaday.spotify.controller;

import com.hanselname.songaday.spotify.dto.TrackSearchDTO;
import com.hanselname.songaday.spotify.service.SpotifyService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/spotify")
public class SpotifyController {
    private final SpotifyService spotifyService;

    public SpotifyController(SpotifyService spotifyService) {
        this.spotifyService = spotifyService;
    }

    @GetMapping("/search-tracks")
    public List<TrackSearchDTO> searchForTracks(@AuthenticationPrincipal(expression = "uuid") UUID appUserUuid, @RequestParam(name = "q") String searchQuery) {
        return spotifyService.searchForTracks(appUserUuid, searchQuery);
    }

    @GetMapping("/search-track/{spotifyId}")
    public TrackSearchDTO searchForTrack(@AuthenticationPrincipal(expression = "uuid") UUID appUserUuid, @PathVariable String spotifyId) {
        return spotifyService.getTrackBySpotifyId(appUserUuid, spotifyId);
    }
}

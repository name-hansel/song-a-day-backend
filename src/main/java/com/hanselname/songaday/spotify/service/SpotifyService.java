package com.hanselname.songaday.spotify.service;

import com.hanselname.songaday.common.CommonUtils;
import com.hanselname.songaday.spotify.dto.TrackSearchDTO;
import com.hanselname.songaday.spotify.mapper.TrackSearchMapper;
import com.hanselname.songaday.spotify.response_model.SpotifySearch;
import com.hanselname.songaday.spotify.response_model.Track;
import com.hanselname.songaday.user.entity.AppUser;
import jakarta.annotation.Nonnull;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SpotifyService {

    private final TrackSearchMapper trackSearchMapper;
    private final WebClient webClient;

    public SpotifyService(TrackSearchMapper trackSearchMapper, WebClient.Builder builder) {
        this.trackSearchMapper = trackSearchMapper;
        this.webClient = builder.baseUrl(CommonUtils.SPOTIFY_BASE_URL).build();
    }

    public List<TrackSearchDTO> searchForTrack(Authentication authentication, @Nonnull String searchQuery) {
        AppUser user = (AppUser) authentication.getPrincipal();

        SpotifySearch searchResponse = webClient.get().uri(uriBuilder -> uriBuilder.path("/search").queryParam("q", searchQuery).queryParam("type", "track").queryParam("limit", "5").build()).headers(headers -> {
            headers.setBearerAuth(user.getSpotifyAccessToken());
        }).retrieve().bodyToMono(SpotifySearch.class).block();
        return extractTracks(searchResponse).stream().map(trackSearchMapper::toDTO).collect(Collectors.toList());
    }

    private List<Track> extractTracks(SpotifySearch searchResult) {
        if (searchResult == null || searchResult.getTracks() == null) {
            return List.of();
        }

        return searchResult.getTracks().getItems();
    }
}

package com.hanselname.songaday.spotify.service;

import com.hanselname.songaday.common.CommonUtils;
import com.hanselname.songaday.spotify.dto.TrackDTO;
import com.hanselname.songaday.spotify.mapper.TrackMapper;
import com.hanselname.songaday.spotify.response_model.SpotifySearch;
import com.hanselname.songaday.spotify.response_model.Track;
import com.hanselname.songaday.user.entity.AppUser;
import jakarta.annotation.Nonnull;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SpotifyService {

    private final TrackMapper trackMapper;
    private final WebClient webClient;

    public SpotifyService(TrackMapper trackMapper, WebClient webClient) {
        this.trackMapper = trackMapper;
        this.webClient = webClient;
    }

    public List<TrackDTO> searchForTrack(Authentication authentication, @Nonnull String searchQuery) {
        AppUser user = (AppUser) authentication.getPrincipal();

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(user.getSpotifyAccessToken());

        SpotifySearch searchResponse = webClient.get().uri(uriBuilder -> uriBuilder.path(CommonUtils.SPOTIFY_BASE_URL).path("/search").queryParam("q", searchQuery).queryParam("type", "track").queryParam("limit", "5").build()).retrieve().bodyToMono(SpotifySearch.class).block();
        return extractTracks(searchResponse).stream().map(trackMapper::toDTO).collect(Collectors.toList());
    }

    private List<Track> extractTracks(SpotifySearch searchResult) {
        if (searchResult == null || searchResult.getTracks() == null) {
            return List.of();
        }

        return searchResult.getTracks().getItems();
    }
}

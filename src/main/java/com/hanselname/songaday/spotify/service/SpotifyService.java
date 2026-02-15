package com.hanselname.songaday.spotify.service;

import com.hanselname.songaday.auth.service.SpotifyTokenService;
import com.hanselname.songaday.common.CommonUtils;
import com.hanselname.songaday.spotify.dto.TrackSearchDTO;
import com.hanselname.songaday.spotify.mapper.TrackSearchMapper;
import com.hanselname.songaday.spotify.response_model.search.SpotifySearch;
import com.hanselname.songaday.spotify.response_model.search.TrackSearch;
import com.hanselname.songaday.user.entity.AppUserEntity;
import jakarta.annotation.Nonnull;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SpotifyService {

    private final TrackSearchMapper trackSearchMapper;
    private final WebClient webClient;
    private final SpotifyTokenService spotifyTokenService;

    public SpotifyService(TrackSearchMapper trackSearchMapper, SpotifyTokenService spotifyTokenService) {
        this.trackSearchMapper = trackSearchMapper;
        this.spotifyTokenService = spotifyTokenService;
        this.webClient = WebClient.builder().baseUrl(CommonUtils.SPOTIFY_BASE_URL).build();
    }

    public List<TrackSearchDTO> searchForTrack(Authentication authentication, @Nonnull String searchQuery) {
        AppUserEntity appUserEntity = (AppUserEntity) authentication.getPrincipal();

        SpotifySearch searchResponse = webClient.get().uri(uriBuilder -> uriBuilder.path("/search").queryParam("q", searchQuery).queryParam("type", "track").queryParam("limit", "5").build()).headers(headers -> {
            headers.setBearerAuth(spotifyTokenService.getValidAccessToken(appUserEntity));
        }).retrieve().bodyToMono(SpotifySearch.class).block();

        return extractTracks(searchResponse).stream().map(trackSearchMapper::toDTO).collect(Collectors.toList());
    }

    public TrackSearchDTO getTrackBySpotifyId(AppUserEntity appUserEntity, @Nonnull String spotifyId) {
        TrackSearch track = webClient.get().uri(uriBuilder -> uriBuilder.path("/tracks/").path(spotifyId).build()).header(HttpHeaders.AUTHORIZATION, "Bearer " + spotifyTokenService.getValidAccessToken(appUserEntity)).retrieve().bodyToMono(TrackSearch.class).block();

        return trackSearchMapper.toDTO(track);
    }

    private List<TrackSearch> extractTracks(SpotifySearch searchResult) {
        if (searchResult == null || searchResult.getTracks() == null) {
            return List.of();
        }

        return searchResult.getTracks().getItems();
    }
}

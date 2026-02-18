package com.hanselname.songaday.spotify.service;

import com.hanselname.songaday.auth.service.SpotifyTokenService;
import com.hanselname.songaday.common.CommonUtils;
import com.hanselname.songaday.spotify.dto.TrackSearchDTO;
import com.hanselname.songaday.spotify.mapper.TrackSearchMapper;
import com.hanselname.songaday.spotify.response_model.search.SpotifySearch;
import com.hanselname.songaday.spotify.response_model.search.TrackSearch;
import com.hanselname.songaday.user.entity.AppUserEntity;
import com.hanselname.songaday.user.repository.AppUserRepository;
import jakarta.annotation.Nonnull;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class SpotifyService {

    private final TrackSearchMapper trackSearchMapper;
    private final WebClient webClient;
    private final SpotifyTokenService spotifyTokenService;
    private final AppUserRepository appUserRepository;

    public SpotifyService(TrackSearchMapper trackSearchMapper, SpotifyTokenService spotifyTokenService, AppUserRepository appUserRepository) {
        this.trackSearchMapper = trackSearchMapper;
        this.spotifyTokenService = spotifyTokenService;
        this.appUserRepository = appUserRepository;
        this.webClient = WebClient.builder().baseUrl(CommonUtils.SPOTIFY_BASE_URL).build();
    }

    public List<TrackSearchDTO> searchForTrack(UUID appUserUuid, @Nonnull String searchQuery) {
        AppUserEntity appUserEntity = appUserRepository.findById(appUserUuid).orElseThrow(() -> new RuntimeException("User not found."));

        SpotifySearch searchResponse = webClient.get().uri(uriBuilder -> uriBuilder.path("/search").queryParam("q", searchQuery).queryParam("type", "track").queryParam("limit", "5").build()).headers(headers -> headers.setBearerAuth(spotifyTokenService.getValidAccessToken(appUserEntity))).retrieve().bodyToMono(SpotifySearch.class).block();

        return extractTracks(searchResponse).stream().map(trackSearchMapper::toDTO).collect(Collectors.toList());
    }

    @Cacheable(value = "spotify:track", key = "#spotifyId")
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

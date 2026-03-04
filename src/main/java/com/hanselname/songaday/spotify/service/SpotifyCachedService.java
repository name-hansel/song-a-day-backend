package com.hanselname.songaday.spotify.service;

import com.hanselname.songaday.auth.service.SpotifyTokenService;
import com.hanselname.songaday.common.CommonUtils;
import com.hanselname.songaday.spotify.dto.TrackSearchDTO;
import com.hanselname.songaday.spotify.exception.TrackNotFoundException;
import com.hanselname.songaday.spotify.mapper.TrackSearchMapper;
import com.hanselname.songaday.spotify.response_model.search.SpotifySearch;
import com.hanselname.songaday.spotify.response_model.search.TrackSearch;
import com.hanselname.songaday.user.entity.AppUserEntity;
import jakarta.annotation.Nonnull;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.List;

@Service
public class SpotifyCachedService {
    private final WebClient webClient;
    private final TrackSearchMapper trackSearchMapper;
    private final SpotifyTokenService spotifyTokenService;

    public SpotifyCachedService(TrackSearchMapper trackSearchMapper, SpotifyTokenService spotifyTokenService) {
        this.trackSearchMapper = trackSearchMapper;
        this.spotifyTokenService = spotifyTokenService;
        this.webClient = WebClient.builder()
                                  .baseUrl(CommonUtils.SPOTIFY_BASE_URL)
                                  .build();
    }

    @Cacheable(value = "spotify:search", key = "#searchQuery")
    public List<TrackSearchDTO> searchForTracks(@Nonnull AppUserEntity appUserEntity, @Nonnull String searchQuery) {
        SpotifySearch searchResponse = webClient.get()
                                                .uri(uriBuilder -> uriBuilder
                                                        .path("/search")
                                                        .queryParam("q",
                                                                searchQuery)
                                                        .queryParam("type",
                                                                "track")
                                                        .queryParam("limit",
                                                                "5").build())
                                                .headers(
                                                        headers -> headers.setBearerAuth(
                                                                spotifyTokenService.getValidAccessToken(
                                                                        appUserEntity)))
                                                .retrieve()
                                                .bodyToMono(SpotifySearch.class)
                                                .block();

        return trackSearchMapper.toDTOList(extractTracks(searchResponse),
                false);
    }

    @Cacheable(value = "spotify:track", key = "#spotifyId")
    public TrackSearchDTO getTrackBySpotifyId(AppUserEntity appUserEntity, @Nonnull String spotifyId, boolean needLargeImage) {
        try {
            return trackSearchMapper.toDTO(
                    getTrackFromSpotify(appUserEntity, spotifyId),
                    needLargeImage);
        } catch (WebClientResponseException.BadRequest exception) {
            throw new TrackNotFoundException();
        }
    }

    private List<TrackSearch> extractTracks(SpotifySearch searchResult) {
        if (searchResult == null || searchResult.getTracks() == null) {
            return List.of();
        }

        return searchResult.getTracks().getItems();
    }

    private TrackSearch getTrackFromSpotify(AppUserEntity appUserEntity, String spotifyId) {
        String validAccessToken = "Bearer " + spotifyTokenService.getValidAccessToken(
                appUserEntity);

        return webClient.get().uri(uriBuilder -> uriBuilder.path("/tracks/")
                                                           .path(spotifyId)
                                                           .build())
                        .header(HttpHeaders.AUTHORIZATION, validAccessToken)
                        .retrieve().bodyToMono(TrackSearch.class).block();
    }
}

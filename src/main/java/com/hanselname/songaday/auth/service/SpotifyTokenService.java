package com.hanselname.songaday.auth.service;

import com.hanselname.songaday.auth.response_model.SpotifyTokenResponse;
import com.hanselname.songaday.common.CommonUtils;
import com.hanselname.songaday.user.entity.AppUserEntity;
import com.hanselname.songaday.user.repository.AppUserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Instant;

@Service
public class SpotifyTokenService {

    @Value("${spring.security.oauth2.client.registration.spotify.client-id}")
    private String clientId;

    private final AppUserRepository appUserRepository;
    private final WebClient spotifyAccountWebClient;

    public SpotifyTokenService(AppUserRepository appUserRepository) {
        this.appUserRepository = appUserRepository;
        this.spotifyAccountWebClient = WebClient.builder().baseUrl(CommonUtils.SPOTIFY_ACCOUNTS_TOKEN_URL).build();
    }

    public String getValidAccessToken(AppUserEntity appUserEntity) {
        if (accessTokenNeedsToBeRefreshed(appUserEntity)) {
            refreshAccessToken(appUserEntity);
        }

        return appUserEntity.getSpotifyAccessToken();
    }

    private boolean accessTokenNeedsToBeRefreshed(AppUserEntity user) {
        return user.getSpotifyTokenExpiresAt().isBefore(Instant.now().plusSeconds(60));
    }

    private void refreshAccessToken(AppUserEntity appUserEntity) {
        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("grant_type", "refresh_token");
        form.add("refresh_token", appUserEntity.getSpotifyRefreshToken());
        form.add("client_id", clientId);

        SpotifyTokenResponse spotifyTokenResponse = spotifyAccountWebClient.post().bodyValue(form).retrieve().bodyToMono(SpotifyTokenResponse.class).block();

        appUserEntity.setSpotifyAccessToken(spotifyTokenResponse.getAccessToken());
        appUserEntity.setSpotifyTokenExpiresAt(Instant.now().plusSeconds(spotifyTokenResponse.getExpiresIn()));
        if (spotifyTokenResponse.getRefreshToken() != null) {
            appUserEntity.setSpotifyRefreshToken(spotifyTokenResponse.getRefreshToken());
        }

        appUserRepository.save(appUserEntity);
    }
}

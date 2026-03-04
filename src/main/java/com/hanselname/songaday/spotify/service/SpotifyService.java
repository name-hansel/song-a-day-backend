package com.hanselname.songaday.spotify.service;

import com.hanselname.songaday.spotify.dto.TrackSearchDTO;
import com.hanselname.songaday.user.entity.AppUserEntity;
import com.hanselname.songaday.user.exception.UserNotFoundException;
import com.hanselname.songaday.user.repository.AppUserRepository;
import jakarta.annotation.Nonnull;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class SpotifyService {
    private final AppUserRepository appUserRepository;
    private final SpotifyCachedService spotifyCachedService;

    public SpotifyService(AppUserRepository appUserRepository, SpotifyCachedService spotifyCachedService) {
        this.appUserRepository = appUserRepository;
        this.spotifyCachedService = spotifyCachedService;
    }

    public List<TrackSearchDTO> searchForTracks(@Nonnull UUID appUserUuid, @Nonnull String searchQuery) {
        return spotifyCachedService.searchForTracks(
                getAppUserEntity(appUserUuid), searchQuery);
    }

    public TrackSearchDTO getTrackBySpotifyId(@Nonnull UUID appUserUuid, @Nonnull String spotifyId) {
        return spotifyCachedService.getTrackBySpotifyId(
                getAppUserEntity(appUserUuid), spotifyId, true);
    }

    public TrackSearchDTO getTrackBySpotifyId(@Nonnull AppUserEntity appUserEntity, @Nonnull String spotifyId, boolean needLargeImage) {
        return spotifyCachedService.getTrackBySpotifyId(appUserEntity,
                spotifyId, needLargeImage);
    }

    private AppUserEntity getAppUserEntity(@Nonnull UUID appUserUuid) {
        return appUserRepository.findById(appUserUuid)
                                .orElseThrow(UserNotFoundException::new);
    }
}

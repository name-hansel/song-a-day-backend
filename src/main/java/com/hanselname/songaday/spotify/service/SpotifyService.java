package com.hanselname.songaday.spotify.service;

import com.hanselname.songaday.user.entity.AppUser;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class SpotifyService {

    public Map<String, Object> getCurrentTrack(Authentication authentication) {
        AppUser user = (AppUser) authentication.getPrincipal();

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(user.getSpotifyAccessToken());
        return new RestTemplate().exchange("https://api.spotify.com/v1/me/player/currently-playing", HttpMethod.GET,
                new HttpEntity<>(headers), Map.class).getBody();
    }
}

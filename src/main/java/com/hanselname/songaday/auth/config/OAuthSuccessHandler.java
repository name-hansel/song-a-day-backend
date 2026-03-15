package com.hanselname.songaday.auth.config;

import com.hanselname.songaday.auth.service.JWTService;
import com.hanselname.songaday.auth.service.RefreshTokenService;
import com.hanselname.songaday.auth.utils.CookieUtils;
import com.hanselname.songaday.spotify.util.SpotifyUtils;
import com.hanselname.songaday.user.entity.AppUserEntity;
import com.hanselname.songaday.user.repository.AppUserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Optional;

@Component
public class OAuthSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Value("${app.frontend-url}")
    private String frontendUrl;

    @Value("${app.frontend-url-new-user}")
    private String frontendUrlNewUser;

    private final AppUserRepository appUserRepository;
    private final JWTService jwtService;
    private final RefreshTokenService refreshTokenService;
    private final OAuth2AuthorizedClientService clientService;
    private final CookieUtils cookieUtils;

    public OAuthSuccessHandler(AppUserRepository appUserRepository, JWTService jwtService, RefreshTokenService refreshTokenService, OAuth2AuthorizedClientService clientService, CookieUtils cookieUtils) {
        this.appUserRepository = appUserRepository;
        this.jwtService = jwtService;
        this.refreshTokenService = refreshTokenService;
        this.clientService = clientService;
        this.cookieUtils = cookieUtils;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {

        OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;
        OAuth2User oauthUser = oauthToken.getPrincipal();
        OAuth2AuthorizedClient client = clientService.loadAuthorizedClient(
                oauthToken.getAuthorizedClientRegistrationId(),
                oauthToken.getName());

        String spotifyId = oauthUser.getAttribute(
                SpotifyUtils.SPOTIFY_ID_ATTRIBUTE_NAME);
        boolean isNewUser = false;

        Optional<AppUserEntity> foundAppUserEntity = appUserRepository.findBySpotifyId(
                spotifyId);
        AppUserEntity appUserEntity = foundAppUserEntity.orElse(null);
        if (appUserEntity == null) {
            appUserEntity = new AppUserEntity();
            appUserEntity.setSpotifyId(spotifyId);
            appUserEntity.setSpotifyEmail(oauthUser.getAttribute(
                    SpotifyUtils.SPOTIFY_EMAIL_ATTRIBUTE_NAME));
            appUserEntity.setSpotifyDisplayName(oauthUser.getAttribute(
                    SpotifyUtils.SPOTIFY_DISPLAY_NAME_ATTRIBUTE_NAME));
            isNewUser = true;
        }

        // Spotify tokens
        appUserEntity.setSpotifyAccessToken(
                client.getAccessToken().getTokenValue());
        appUserEntity.setSpotifyRefreshToken(
                client.getRefreshToken().getTokenValue());
        appUserEntity.setSpotifyTokenExpiresAt(
                client.getAccessToken().getExpiresAt());

        appUserRepository.save(appUserEntity);

        // Tokens for frontend
        String accessToken = jwtService.generateAccessToken(
                appUserEntity.getUuid());
        String refreshToken = refreshTokenService.generateRefreshTokenForAppUser(
                appUserEntity.getUuid());

        response.addHeader(HttpHeaders.SET_COOKIE,
                cookieUtils.createAccessTokenCookie(accessToken).toString());
        response.addHeader(HttpHeaders.SET_COOKIE,
                cookieUtils.createRefreshTokenCookie(refreshToken).toString());

        getRedirectStrategy().sendRedirect(request, response,
                isNewUser ? frontendUrlNewUser : frontendUrl);
    }
}

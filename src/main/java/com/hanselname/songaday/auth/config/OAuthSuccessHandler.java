package com.hanselname.songaday.auth.config;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.hanselname.songaday.auth.service.JWTService;
import com.hanselname.songaday.auth.utils.AuthUtils;
import com.hanselname.songaday.spotify.util.SpotifyUtils;
import com.hanselname.songaday.user.entity.AppUser;
import com.hanselname.songaday.user.repository.AppUserRepository;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class OAuthSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

	@Value("${app.jwt.expiration}")
	private long expiration;

	@Value("${app.frontend-url}")
	private String frontendUrl;

	private final AppUserRepository appUserRepository;
	private final JWTService jwtService;
	private final OAuth2AuthorizedClientService clientService;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {

		OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;
		OAuth2User oauthUser = oauthToken.getPrincipal();
		OAuth2AuthorizedClient client = clientService
				.loadAuthorizedClient(oauthToken.getAuthorizedClientRegistrationId(), oauthToken.getName());

		String spotifyId = oauthUser.getAttribute(SpotifyUtils.SPOTIFY_ID_ATTRIBUTE_NAME);
		AppUser user = appUserRepository.findBySpotifyId(spotifyId).orElseGet(() -> {
			AppUser newUser = new AppUser();
			newUser.setSpotifyId(spotifyId);
			newUser.setSpotifyEmail(oauthUser.getAttribute(SpotifyUtils.SPOTIFY_EMAIL_ATTRIBUTE_NAME));
			newUser.setSpotifyDisplayName(oauthUser.getAttribute(SpotifyUtils.SPOTIFY_DISPLAY_NAME_ATTRIBUTE_NAME));

			return newUser;
		});

		user.setSpotifyAccessToken(client.getAccessToken().getTokenValue());
		user.setSpotifyRefreshToken(client.getRefreshToken().getTokenValue());
		user.setSpotifyTokenExpiresAt(client.getAccessToken().getExpiresAt());

		appUserRepository.save(user);

		Cookie cookie = new Cookie(AuthUtils.COOKIE_NAME, jwtService.generate(user.getUuid()));
		cookie.setHttpOnly(true);
		cookie.setPath("/");
		cookie.setMaxAge((int) (expiration / 100));

		response.addCookie(cookie);
		getRedirectStrategy().sendRedirect(request, response, frontendUrl);
	}
}

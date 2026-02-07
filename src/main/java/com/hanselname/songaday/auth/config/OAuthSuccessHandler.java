package com.hanselname.songaday.auth.config;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.hanselname.songaday.auth.service.JWTService;
import com.hanselname.songaday.auth.utils.AuthUtils;
import com.hanselname.songaday.user.entity.AppUser;
import com.hanselname.songaday.user.repository.UserRepository;

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

	private final UserRepository userRepository;
	private final JWTService jwtService;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		OAuth2User oAuthUser = (OAuth2User) authentication.getPrincipal();

		String spotifyId = oAuthUser.getAttribute("id");
		String email = oAuthUser.getAttribute("email");
		String name = oAuthUser.getAttribute("display_name");

		AppUser user = userRepository.findBySpotifyId(spotifyId).orElseGet(() -> {
			AppUser newUser = new AppUser();
			newUser.setSpotifyId(spotifyId);
			newUser.setEmail(email);
			newUser.setDisplayName(name);
			return userRepository.save(newUser);
		});

		String jwt = jwtService.generate(user.getId());

		Cookie cookie = new Cookie(AuthUtils.COOKIE_NAME, jwt);
		cookie.setHttpOnly(true);
		cookie.setPath("/");
		cookie.setMaxAge((int) (expiration / 100));

		response.addCookie(cookie);
		getRedirectStrategy().sendRedirect(request, response, frontendUrl);
	}
}

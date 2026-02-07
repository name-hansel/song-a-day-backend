package com.hanselname.songaday.auth.filter;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.hanselname.songaday.auth.service.JWTService;
import com.hanselname.songaday.auth.utils.AuthUtils;
import com.hanselname.songaday.user.entity.AppUser;
import com.hanselname.songaday.user.repository.UserRepository;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JWTFilter extends OncePerRequestFilter {
	private final JWTService jwtService;
	private final UserRepository userRepository;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		if (request.getCookies() != null) {
			for (Cookie cookie : request.getCookies()) {
				if (cookie.getName().equals(AuthUtils.COOKIE_NAME)) {
					UUID userUUID = jwtService.validate(cookie.getValue());
					AppUser user = userRepository.findById(userUUID).orElse(null);

					if (user != null) {
						UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(user,
								null, List.of());
						SecurityContextHolder.getContext().setAuthentication(authToken);
					}
				}
			}
		}

		filterChain.doFilter(request, response);
	}
}

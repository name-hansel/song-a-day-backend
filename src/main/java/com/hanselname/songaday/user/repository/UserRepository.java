package com.hanselname.songaday.user.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hanselname.songaday.user.entity.AppUser;

public interface UserRepository extends JpaRepository<AppUser, UUID> {
	Optional<AppUser> findBySpotifyId(String spotifyId);
}

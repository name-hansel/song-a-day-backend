package com.hanselname.songaday.song.dto;

import java.time.LocalDate;

public record SongRequestDTO(String spotifyId, String memory, LocalDate date) {
}
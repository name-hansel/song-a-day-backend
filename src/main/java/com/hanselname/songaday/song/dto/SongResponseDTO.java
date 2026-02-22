package com.hanselname.songaday.song.dto;

import com.hanselname.songaday.spotify.dto.TrackSearchDTO;

import java.time.LocalDate;
import java.util.UUID;

public record SongResponseDTO(UUID uuid, LocalDate songDate,
                              TrackSearchDTO trackInformation,
                              String addedAtTime) {
}

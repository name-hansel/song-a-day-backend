package com.hanselname.songaday.song.dto;

import java.time.LocalDate;
import java.util.List;

public record SongHistoryResponse(List<SongResponseDTO> songHistory, LocalDate nextDate, boolean hasMore) {
}

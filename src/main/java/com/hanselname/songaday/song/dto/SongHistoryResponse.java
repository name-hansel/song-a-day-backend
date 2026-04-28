package com.hanselname.songaday.song.dto;

import java.time.LocalDate;
import java.util.List;

public record SongHistoryResponse(List<SongResponseDTO> history, LocalDate nextDate, LocalDate previousDate,
                                  boolean hasMoreNext, boolean hasMorePrevious) {
}

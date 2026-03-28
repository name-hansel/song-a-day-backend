package com.hanselname.songaday.song.controller;

import com.hanselname.songaday.common.utils.CommonUtils;
import com.hanselname.songaday.song.dto.SongHistoryResponse;
import com.hanselname.songaday.song.dto.SongRequestDTO;
import com.hanselname.songaday.song.dto.SongResponseDTO;
import com.hanselname.songaday.song.dto.UpdateMemoryRequestDTO;
import com.hanselname.songaday.song.service.SongService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(path = CommonUtils.SONG_API_PREFIX)
public class SongController {

    private final SongService songService;

    public SongController(SongService songService) {
        this.songService = songService;
    }

    @GetMapping("/{date}")
    public SongResponseDTO getSongOfDay(@AuthenticationPrincipal(expression = "uuid") UUID appUserUuid, @PathVariable String date) {
        return songService.getSongOfDay(appUserUuid, date);
    }

    @GetMapping
    public SongResponseDTO getSongOfDay(@AuthenticationPrincipal(expression = "uuid") UUID appUserUuid) {
        return songService.getSongOfDay(appUserUuid);
    }

    @PutMapping
    public SongResponseDTO logSongOfDay(@AuthenticationPrincipal(expression = "uuid") UUID appUserUuid, @RequestBody SongRequestDTO request) {
        return songService.logSongOfDay(appUserUuid, request);
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteSongOfDay(@AuthenticationPrincipal(expression = "uuid") UUID appUserUuid) {
        songService.deleteSongOfDay(appUserUuid);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/weekly-history")
    public List<SongResponseDTO> getSongHistoryForLastWeek(@AuthenticationPrincipal(expression = "uuid") UUID appUserUuid) {
        return songService.getSongHistoryForLastWeek(appUserUuid);
    }

    @PatchMapping("/memory/{songUuid}")
    public SongResponseDTO updateMemoryForSong(@AuthenticationPrincipal(expression = "uuid") UUID appUserUuid, @PathVariable UUID songUuid, @RequestBody UpdateMemoryRequestDTO request) {
        return songService.updateMemoryForSong(appUserUuid, songUuid, request);
    }

    @GetMapping("/history")
    public SongHistoryResponse getSongHistory(@AuthenticationPrincipal(expression = "uuid") UUID appUserUuid, @RequestParam(required = false) LocalDate beforeDate, @RequestParam(defaultValue = "30") int limit) {
        return songService.getSongHistory(appUserUuid, beforeDate, limit);
    }
}

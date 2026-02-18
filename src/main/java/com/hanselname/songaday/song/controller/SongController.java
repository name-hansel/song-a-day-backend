package com.hanselname.songaday.song.controller;

import com.hanselname.songaday.common.CommonUtils;
import com.hanselname.songaday.song.dto.SongRequestDTO;
import com.hanselname.songaday.song.dto.SongResponseDTO;
import com.hanselname.songaday.song.service.SongService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping(path = CommonUtils.SONG_API_PREFIX)
public class SongController {

    private final SongService songService;

    public SongController(SongService songService) {
        this.songService = songService;
    }

    @GetMapping("/{day}-{month}-{year}")
    public SongResponseDTO getSongOfDay(@AuthenticationPrincipal(expression = "uuid") UUID appUserUuid, @PathVariable(name = "day") int dayOfMonth, @PathVariable int month, @PathVariable int year) {
        return songService.getSongOfDay(appUserUuid, dayOfMonth, month, year);
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
}

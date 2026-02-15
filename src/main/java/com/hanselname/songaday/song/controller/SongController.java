package com.hanselname.songaday.song.controller;

import com.hanselname.songaday.common.CommonUtils;
import com.hanselname.songaday.song.dto.SongRequestDTO;
import com.hanselname.songaday.song.dto.SongResponseDTO;
import com.hanselname.songaday.song.service.SongService;
import com.hanselname.songaday.user.entity.AppUserEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = CommonUtils.SONG_API_PREFIX)
public class SongController {

    private final SongService songService;

    public SongController(SongService songService) {
        this.songService = songService;
    }

    @GetMapping("/{day}-{month}-{year}")
    public ResponseEntity<SongResponseDTO> getSongOfDay(@AuthenticationPrincipal AppUserEntity appUserEntity, @PathVariable(name = "day") int dayOfMonth, @PathVariable int month, @PathVariable int year) {
        try {
            return ResponseEntity.ok(songService.getSongOfDay(appUserEntity, dayOfMonth, month, year));
        } catch (Exception exc) {
            // TODO: handle messages
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping
    public ResponseEntity<SongResponseDTO> getSongOfDay(@AuthenticationPrincipal AppUserEntity appUserEntity) {
        return ResponseEntity.ok(songService.getSongOfDay(appUserEntity));
    }

    @PostMapping
    public ResponseEntity<SongResponseDTO> logSongOfDay(@AuthenticationPrincipal AppUserEntity appUserEntity, @RequestBody SongRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(songService.logSongOfDay(appUserEntity.getUuid(), request));
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteSongOfDay(@AuthenticationPrincipal AppUserEntity appUserEntity) {
        songService.deleteSongOfDay(appUserEntity.getUuid());
        return ResponseEntity.noContent().build();
    }

    @PatchMapping
    public ResponseEntity<SongResponseDTO> updateSongOfDay(@AuthenticationPrincipal AppUserEntity appUserEntity, @RequestBody SongRequestDTO request) {
        return ResponseEntity.status(HttpStatus.OK).body(songService.updateSongOfDay(appUserEntity, request));
    }
}

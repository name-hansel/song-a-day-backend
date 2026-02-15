package com.hanselname.songaday.song.controller;

import com.hanselname.songaday.common.CommonUtils;
import com.hanselname.songaday.song.dto.SongLogDTO;
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
    public ResponseEntity<?> getSongOfDay(@AuthenticationPrincipal AppUserEntity appUserEntity, @PathVariable(name = "day") int dayOfMonth, @PathVariable int month, @PathVariable int year) {
        try {
            return ResponseEntity.ok(songService.getSongOfDay(appUserEntity.getUuid(), dayOfMonth, month, year));
        } catch (Exception exc) {
            return ResponseEntity.badRequest().body(exc.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<?> logSongOfDay(@AuthenticationPrincipal AppUserEntity appUserEntity, @RequestBody SongLogDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(songService.logSongOfDay(appUserEntity.getUuid(), request));
    }
}

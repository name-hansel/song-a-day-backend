package com.hanselname.songaday.song.controller;

import com.hanselname.songaday.song.service.SongService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/song-of-day")
public class SongController {

    private final SongService songService;

    public SongController(SongService songService) {
        this.songService = songService;
    }

    @GetMapping("/{day}-{month}-{year}")
    public ResponseEntity<?> getSongOfDay(Authentication authentication, @PathVariable(name = "day") int dayOfMonth, @PathVariable int month, @PathVariable int year) {
        try {
            return ResponseEntity.ok(songService.getSongOfDay(authentication, dayOfMonth, month, year));
        } catch (Exception exc) {
            return ResponseEntity.badRequest().body(exc.getMessage());
        }
    }
}

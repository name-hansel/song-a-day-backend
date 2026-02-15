package com.hanselname.songaday.song.controller;

import com.hanselname.songaday.song.service.SongService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.DateTimeException;
import java.time.LocalDate;

@RestController
@RequestMapping("/api/song-of-day")
public class SongController {

    private final SongService songService;

    public SongController(SongService songService) {
        this.songService = songService;
    }

    @GetMapping("/{day}-{month}-{year}")
    public ResponseEntity<?> getSongOfDay(@PathVariable(name = "day") int dayOfMonth, @PathVariable int month, @PathVariable int year) {
        try {
            return ResponseEntity.ok(songService.getSongOfDay(LocalDate.of(year, month, dayOfMonth)));
        } catch (DateTimeException exc) {
            return ResponseEntity.badRequest().build();
        }
    }
}

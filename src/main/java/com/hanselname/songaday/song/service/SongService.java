package com.hanselname.songaday.song.service;

import com.hanselname.songaday.song.entity.SongEntity;
import com.hanselname.songaday.song.repository.SongRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class SongService {

    private final SongRepository songRepository;

    public SongService(SongRepository songRepository) {
        this.songRepository = songRepository;
    }

    public Optional<SongEntity> getSongOfDay(LocalDate date) {
        return songRepository.findBySongDate(date);
    }
}

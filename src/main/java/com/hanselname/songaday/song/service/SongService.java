package com.hanselname.songaday.song.service;

import com.hanselname.songaday.song.entity.SongEntity;
import com.hanselname.songaday.song.repository.SongRepository;
import com.hanselname.songaday.user.entity.AppUserEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class SongService {

    private final SongRepository songRepository;

    public SongService(SongRepository songRepository) {
        this.songRepository = songRepository;
    }

    public Optional<SongEntity> getSongOfDay(Authentication authentication, int day, int month, int year) {
        AppUserEntity appUserEntity = (AppUserEntity) authentication.getPrincipal();
        return songRepository.findByAppUserUuidAndSongDate(appUserEntity.getUuid(), LocalDate.of(year, month, day));
    }
}

package com.hanselname.songaday.song.entity;

import com.hanselname.songaday.common.AbstractEntity;
import com.hanselname.songaday.user.entity.AppUserEntity;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.UUID;

@Entity
public class SongEntity extends AbstractEntity {

    @GeneratedValue
    @Id
    private UUID uuid;

    @ManyToOne(optional = false)
    private AppUserEntity appUserEntity;

    @Column(nullable = false)
    private String spotifyId;

    @Column(nullable = false)
    private LocalDate songDate;
}

package com.hanselname.songaday.song.entity;

import com.hanselname.songaday.common.entity.AbstractEntity;
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
    @JoinColumn(name = "app_user_entity_uuid", nullable = false)
    private AppUserEntity appUser;

    @Column(nullable = false)
    private String spotifyId;

    @Column(nullable = false)
    private LocalDate songDate;

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public AppUserEntity getAppUser() {
        return appUser;
    }

    public void setAppUser(AppUserEntity appUser) {
        this.appUser = appUser;
    }

    public String getSpotifyId() {
        return spotifyId;
    }

    public void setSpotifyId(String spotifyId) {
        this.spotifyId = spotifyId;
    }

    public LocalDate getSongDate() {
        return songDate;
    }

    public void setSongDate(LocalDate songDate) {
        this.songDate = songDate;
    }
}

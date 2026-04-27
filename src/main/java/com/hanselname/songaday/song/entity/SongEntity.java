package com.hanselname.songaday.song.entity;

import com.hanselname.songaday.common.entity.AbstractEntity;
import com.hanselname.songaday.user.entity.AppUserEntity;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(indexes = {@Index(name = "idx_app_user_song_date", columnList = "app_user_entity_uuid, song_date")}, uniqueConstraints = {@UniqueConstraint(columnNames = {"app_user_entity_uuid", "song_date"})})
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

    @Column(length = 200)
    private String memory;

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

    public String getMemory() {
        return memory;
    }

    public void setMemory(String memory) {
        this.memory = memory;
    }
}

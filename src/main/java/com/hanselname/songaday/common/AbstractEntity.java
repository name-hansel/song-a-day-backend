package com.hanselname.songaday.common;

import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class AbstractEntity {
    @CreatedDate
    protected Instant creatingDate;

    @LastModifiedDate
    protected Instant updatingDate;

    @CreatedBy
    protected String creatingUserSpotifyId;

    @LastModifiedBy
    protected String updatingUserSpotifyId;
}

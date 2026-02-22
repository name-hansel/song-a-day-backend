package com.hanselname.songaday.common.entity;

import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import org.springframework.data.annotation.CreatedDate;
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

    public Instant getCreatingDate() {
        return creatingDate;
    }

    public void setCreatingDate(Instant creatingDate) {
        this.creatingDate = creatingDate;
    }

    public Instant getUpdatingDate() {
        return updatingDate;
    }

    public void setUpdatingDate(Instant updatingDate) {
        this.updatingDate = updatingDate;
    }
}

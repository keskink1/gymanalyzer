package com.keskin.gymanalyzer.common.model;

import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Getter
public abstract class BaseEntity {

    private final UUID uuid;
    private final LocalDateTime createdAt;
    private final String createdBy;

    private LocalDateTime updatedAt;
    private String updatedBy;
    private LocalDateTime deletedAt;
    private String deletedBy;
    private boolean deleted;

    //For creating new entity
    protected BaseEntity(String createdBy) {
        this.uuid = UUID.randomUUID();
        this.createdAt = LocalDateTime.now();
        this.createdBy = Objects.requireNonNull(createdBy, "createdBy cannot be null");
        this.deleted = false;
    }

    // Calls from db
    protected BaseEntity(UUID uuid, LocalDateTime createdAt, String createdBy, boolean deleted, LocalDateTime deletedAt, String deletedBy,
                         LocalDateTime updatedAt, String updatedBy) {
        this.uuid = Objects.requireNonNull(uuid, "uuid cannot be null");
        this.createdAt = Objects.requireNonNull(createdAt, "createdAt cannot be null");
        this.createdBy = Objects.requireNonNull(createdBy, "createdBy cannot be null");
        this.deleted = deleted;
        this.deletedAt = deletedAt;
        this.deletedBy = deletedBy;
        this.updatedAt = updatedAt;
        this.updatedBy = updatedBy;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BaseEntity other)) return false;
        return uuid.equals(other.uuid);
    }

    @Override
    public int hashCode() {
        return uuid.hashCode();
    }

    protected void markAsDeleted(String deletedBy) {
        this.deleted = true;
        this.deletedAt = LocalDateTime.now();
        this.deletedBy = Objects.requireNonNull(deletedBy, "deletedBy cannot be null");
    }

    protected void updateAudit(String updatedBy) {
        this.updatedAt = LocalDateTime.now();
        this.updatedBy = Objects.requireNonNull(updatedBy, "updatedBy cannot be null");
    }
}
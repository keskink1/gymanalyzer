package com.keskin.gymanalyzer.users.infrastructure.entity;

import com.keskin.gymanalyzer.users.domain.model.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(
        name = "app_users",
        indexes = {
                @Index(name = "idx_user_email",         columnList = "email"),
                @Index(name = "idx_user_email_deleted",  columnList = "email, deleted")
        })
public class UserEntity {

        @Id
        @Column(name = "user_id", updatable = false, nullable = false)
        private UUID uuid;

        @Column(name = "first_name", nullable = false)
        private String firstName;

        @Column(name = "last_name", nullable = false)
        private String lastName;

        @Column(name = "email", nullable = false, unique = true)
        private String email;

        @Column(name = "password", nullable = false)
        private String password;

        @Column(name = "value")
        private Integer age;

        @Enumerated(EnumType.STRING)
        @Column(name = "role", nullable = false)
        private Role role;

        @Column(name = "active", nullable = false)
        private boolean active;

        // -- BASE ENTITY --
        @Column(name = "created_at", nullable = false, updatable = false)
        private LocalDateTime createdAt;

        @Column(name = "created_by", nullable = false, updatable = false)
        private String createdBy;

        @Column(name = "updated_at")
        private LocalDateTime updatedAt;

        @Column(name = "updated_by")
        private String updatedBy;

        @Column(name = "deleted", nullable = false)
        private boolean deleted = false;

        @Column(name = "deleted_at")
        private LocalDateTime deletedAt;

        @Column(name = "deleted_by")
        private String deletedBy;
}

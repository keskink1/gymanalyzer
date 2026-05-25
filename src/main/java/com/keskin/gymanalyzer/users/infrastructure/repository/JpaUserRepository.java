package com.keskin.gymanalyzer.users.infrastructure.repository;

import com.keskin.gymanalyzer.users.domain.model.User;
import com.keskin.gymanalyzer.users.infrastructure.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface JpaUserRepository extends JpaRepository<UserEntity, UUID> {
    boolean existsByEmailAndDeletedFalse(String email);

    Optional<UserEntity> findByEmail(String email);

    Optional<UserEntity> findByEmailAndDeletedFalse(String email);
}

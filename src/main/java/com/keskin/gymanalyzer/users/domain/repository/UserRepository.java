package com.keskin.gymanalyzer.users.domain.repository;

import com.keskin.gymanalyzer.common.dto.PaginatedResponseDto;
import com.keskin.gymanalyzer.users.domain.model.User;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository {
    PaginatedResponseDto<User> findAllUsers(int page, int size);

    Optional<User> findById(UUID uuid);

    Optional<User> findByEmail(String email);

    void saveUser(User user);

    boolean existsByEmailAndDeletedFalse(String email);
}

package com.keskin.gymanalyzer.users.domain.repository;

import java.util.Optional;
import java.util.UUID;

public interface TokenStore {
    void save(String refreshToken, UUID userUuid, long ttlSeconds);
    Optional<UUID> findUserUuidByRefreshToken(String refreshToken);
    void delete(String refreshToken);
}

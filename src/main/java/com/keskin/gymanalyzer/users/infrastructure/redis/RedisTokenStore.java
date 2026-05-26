package com.keskin.gymanalyzer.users.infrastructure.redis;

import com.keskin.gymanalyzer.users.domain.repository.TokenStore;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class RedisTokenStore implements TokenStore {

    private final StringRedisTemplate redisTemplate;

    // The prefix prevents clashing with other Redis keys
    private static final String PREFIX = "rt:";

    @Override
    public void save(String refreshToken, UUID userUuid, long ttlSeconds) {
        redisTemplate.opsForValue().set(
                PREFIX + refreshToken,
                userUuid.toString(),
                ttlSeconds,      // how long before Redis auto-deletes it
                TimeUnit.SECONDS
        );
    }

    @Override
    public Optional<UUID> findUserUuidByRefreshToken(String refreshToken) {
        String value = redisTemplate.opsForValue().get(PREFIX + refreshToken);

        if (value == null) return Optional.empty();

        return Optional.of(UUID.fromString(value));
    }

    @Override
    public void delete(String refreshToken) {
        redisTemplate.delete(PREFIX + refreshToken);
    }
}
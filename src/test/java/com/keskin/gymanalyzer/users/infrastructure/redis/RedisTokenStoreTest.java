package com.keskin.gymanalyzer.users.infrastructure.redis;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;



@ExtendWith(MockitoExtension.class)
class RedisTokenStoreTest {

    @Mock
    private StringRedisTemplate redisTemplate;

    @Mock
    private ValueOperations<String, String> valueOperations;

    @InjectMocks
    private RedisTokenStore tokenStore;



    @Test
    void shouldSaveRefreshTokenWithCorrectKeyAndTtl() {
        when(redisTemplate.opsForValue()).thenReturn(valueOperations); // ← here
        UUID userUuid = UUID.randomUUID();

        tokenStore.save("mytoken", userUuid, 604800L);

        verify(valueOperations).set("rt:mytoken", userUuid.toString(), 604800L, TimeUnit.SECONDS);
    }

    @Test
    void shouldReturnUserUuidWhenTokenExists() {
        when(redisTemplate.opsForValue()).thenReturn(valueOperations); // ← here
        UUID userUuid = UUID.randomUUID();
        when(valueOperations.get("rt:mytoken")).thenReturn(userUuid.toString());

        Optional<UUID> result = tokenStore.findUserUuidByRefreshToken("mytoken");

        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(userUuid);
    }

    @Test
    void shouldReturnEmptyWhenTokenNotFound() {
        when(redisTemplate.opsForValue()).thenReturn(valueOperations); // ← here
        when(valueOperations.get("rt:unknowntoken")).thenReturn(null);

        Optional<UUID> result = tokenStore.findUserUuidByRefreshToken("unknowntoken");

        assertThat(result).isEmpty();
    }

    @Test
    void shouldDeleteTokenFromRedis() {
        tokenStore.delete("mytoken");

        verify(redisTemplate).delete("rt:mytoken");
    }
}
package com.keskin.gymanalyzer.users.infrastructure.jwt;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


@ExtendWith(MockitoExtension.class)
class JwtServiceTest {

    private JwtService jwtService;

    @BeforeEach
    void setUp() {
        jwtService = new JwtService();

        // Manually inject @Value fields since we're not using Spring context
        ReflectionTestUtils.setField(jwtService, "secretKey",
                "dGVzdFNlY3JldEtleUZvckp3dFRlc3RpbmdQdXJwb3Nlcw==");
        ReflectionTestUtils.setField(jwtService, "accessTokenExpiration", 900000L);
    }

    @Test
    void shouldGenerateValidAccessToken() {
        String token = jwtService.generateAccessToken("user@email.com", "USER");

        assertThat(jwtService.isTokenValid(token)).isTrue();
        assertThat(jwtService.extractEmail(token)).isEqualTo("user@email.com");
    }

    @Test
    void shouldReturnFalseForInvalidToken() {
        assertThat(jwtService.isTokenValid("thisisnotalidtoken")).isFalse();
    }
}

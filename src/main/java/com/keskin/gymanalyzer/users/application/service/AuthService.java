package com.keskin.gymanalyzer.users.application.service;

import com.keskin.gymanalyzer.common.exception.AuthenticationException;
import com.keskin.gymanalyzer.common.exception.ResourceAlreadyExistsException;
import com.keskin.gymanalyzer.common.exception.ResourceNotFoundException;
import com.keskin.gymanalyzer.users.application.dto.UserDto;
import com.keskin.gymanalyzer.users.application.dto.auth.AuthResponse;
import com.keskin.gymanalyzer.users.application.dto.auth.LoginRequestDto;
import com.keskin.gymanalyzer.users.application.dto.auth.RegisterRequestDto;
import com.keskin.gymanalyzer.users.application.mapper.UserMapper;
import com.keskin.gymanalyzer.users.domain.model.User;
import com.keskin.gymanalyzer.users.domain.repository.TokenStore;
import com.keskin.gymanalyzer.users.domain.repository.UserRepository;
import com.keskin.gymanalyzer.users.domain.valueobject.Age;
import com.keskin.gymanalyzer.users.domain.valueobject.Email;
import com.keskin.gymanalyzer.users.domain.valueobject.FullName;
import com.keskin.gymanalyzer.users.domain.valueobject.Password;
import com.keskin.gymanalyzer.users.infrastructure.jwt.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final UserRepository userRepository;
    private final TokenStore tokenStore;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    @Value("${redis.refresh-token.ttl}")
    private long refreshTokenTtl;

    private String generateAccessToken(User user) {
        return jwtService.generateAccessToken(
                user.getUuid().toString(),
                user.getEmail().getValue()
        );
    }

    private String generateRefreshToken(User user) {
        return jwtService.generateRefreshToken(
                user.getUuid().toString(),
                user.getEmail().getValue()
        );
    }

    private AuthResponse generateAuthResponse(User user) {
        String accessToken  = jwtService.generateAccessToken(
                user.getEmail().getValue(),
                user.getRole().name()
        );

        String refreshToken = UUID.randomUUID().toString();

        // store refresh token in redis with TTL
        tokenStore.save(refreshToken, user.getUuid(), refreshTokenTtl);

        return new AuthResponse(accessToken, refreshToken);
    }

    private void assertEmailNotTaken(String email){
        boolean isEmailActive = userRepository.existsByEmailAndDeletedFalse(email);
        if (isEmailActive) {
            throw new ResourceAlreadyExistsException("User", "Email", email);
        }
    }


    @Transactional
    public AuthResponse register(RegisterRequestDto requestDto) {
        assertEmailNotTaken(requestDto.email());

        Password rawPassword = Password.rawValidated(requestDto.password());

        String hashedPassword = passwordEncoder.encode(rawPassword.getValue());

        User newUser = User.createUser(
                "self", // change after jwt
                new FullName(requestDto.firstName(), requestDto.lastName()),
                new Age(requestDto.age()),
                new Email(requestDto.email()),
                Password.hashed(hashedPassword)
        );

        // send welcome mail

        userRepository.saveUser(newUser);

        String accessToken = generateAccessToken(newUser);
        String refreshToken = generateRefreshToken(newUser);

        return new AuthResponse(accessToken, refreshToken);
    }

    public AuthResponse login(LoginRequestDto request) {
        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new AuthenticationException("Invalid email or password"));

        // passwordEncoder.matches(plaintext, hash). should change the value object towards this too
        if (!passwordEncoder.matches(request.password(), user.getPassword().getValue())) {
            throw new AuthenticationException("Invalid email or password");
        }

        log.info("User logged in successfully. userId={}", user.getUuid());
        return generateAuthResponse(user);
    }

    public AuthResponse refresh(String refreshToken) {
        UUID userUuid = tokenStore.findUserUuidByRefreshToken(refreshToken)
                .orElseThrow(() -> new AuthenticationException("Invalid or expired refresh token"));

        User user = userRepository.findById(userUuid)
                .orElseThrow(() -> new ResourceNotFoundException("User", "ID", userUuid.toString()));

        tokenStore.delete(refreshToken);

        return generateAuthResponse(user);
    }

    public void logout(String refreshToken) {
        // just delete from redis, token is immediately revoked
        tokenStore.delete(refreshToken);
        log.info("User logged out, refresh token revoked");
    }


}
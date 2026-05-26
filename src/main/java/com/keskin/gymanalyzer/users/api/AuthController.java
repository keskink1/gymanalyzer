package com.keskin.gymanalyzer.users.api;

import com.keskin.gymanalyzer.users.application.dto.auth.AuthResponse;
import com.keskin.gymanalyzer.users.application.dto.auth.LoginRequestDto;
import com.keskin.gymanalyzer.users.application.dto.auth.RefreshRequest;
import com.keskin.gymanalyzer.users.application.dto.auth.RegisterRequestDto;
import com.keskin.gymanalyzer.users.application.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequestDto request) {

        AuthResponse response = authService.register(request);

        URI location = URI.create("/api/v1/auth/register");

        return ResponseEntity
                .created(location)
                .body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequestDto request) {
        AuthResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refresh(@RequestBody RefreshRequest request) {
        AuthResponse response = authService.refresh(request.refreshToken());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestBody RefreshRequest request) {
        authService.logout(request.refreshToken());
        return ResponseEntity.noContent().build();
    }
}
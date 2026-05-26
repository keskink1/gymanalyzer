package com.keskin.gymanalyzer.users.application.dto.auth;

public record RegisterRequestDto(
        String firstName,
        String lastName,
        int age,
        String email,
        String password
) {
}

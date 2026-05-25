package com.keskin.gymanalyzer.users.application.dto;

public record UserRegisterRequestDto(
        String firstName,
        String lastName,
        int age,
        String email,
        String password
) {
}

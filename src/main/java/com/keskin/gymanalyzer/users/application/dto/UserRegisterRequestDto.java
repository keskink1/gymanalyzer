package com.keskin.gymanalyzer.users.application.dto;

import com.keskin.gymanalyzer.users.domain.model.Role;

public record UserRegisterRequestDto(
        String fullName,
        String email,
        int age,
        Role role
) {
}

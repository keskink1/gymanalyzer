package com.keskin.gymanalyzer.users.application.dto;

import com.keskin.gymanalyzer.users.domain.model.Role;

import java.util.UUID;

public record UserDto(
        UUID uuid,
        String fullName,
        String email,
        int age,
        Role role
) {
}

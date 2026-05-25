package com.keskin.gymanalyzer.users.application.mapper;

import com.keskin.gymanalyzer.users.application.dto.UserDto;
import com.keskin.gymanalyzer.users.application.dto.UserRegisterRequestDto;
import com.keskin.gymanalyzer.users.domain.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserDto toResponse(User user) {
        return new UserDto(
                user.getUuid(),
                user.getFullName().getDisplayName(),
                user.getEmail().getValue(),
                user.getAge().value(),
                user.getRole()
        );
    }
}
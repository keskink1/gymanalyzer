package com.keskin.gymanalyzer.users.application.service;

import com.keskin.gymanalyzer.common.exception.ResourceNotFoundException;
import com.keskin.gymanalyzer.users.application.dto.UserDto;
import com.keskin.gymanalyzer.users.application.mapper.UserMapper;
import com.keskin.gymanalyzer.users.domain.model.User;
import com.keskin.gymanalyzer.users.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserAppService {

    private final UserMapper userMapper;
    private final UserRepository userRepository;

    private String getAuditor() {
        return "";
    }

    @Transactional(readOnly = true)
    public UserDto getUserDtoById(UUID uuid) {
        User user = userRepository.findById(uuid).orElseThrow(
                () -> new ResourceNotFoundException("User", "ID", uuid.toString())

        );
        return userMapper.toResponse(user);
    }

    @Transactional(readOnly = true)
    public UserDto getAllUsers() {

    }
}

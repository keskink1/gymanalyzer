package com.keskin.gymanalyzer.users.application.service;

import com.keskin.gymanalyzer.common.dto.PaginatedResponseDto;
import com.keskin.gymanalyzer.common.exception.ResourceAlreadyExistsException;
import com.keskin.gymanalyzer.common.exception.ResourceNotFoundException;
import com.keskin.gymanalyzer.users.application.dto.*;
import com.keskin.gymanalyzer.users.application.dto.auth.RegisterRequestDto;
import com.keskin.gymanalyzer.users.application.mapper.UserMapper;
import com.keskin.gymanalyzer.users.domain.model.User;
import com.keskin.gymanalyzer.users.domain.repository.UserRepository;
import com.keskin.gymanalyzer.users.domain.valueobject.Age;
import com.keskin.gymanalyzer.users.domain.valueobject.Email;
import com.keskin.gymanalyzer.users.domain.valueobject.FullName;
import com.keskin.gymanalyzer.users.domain.valueobject.Password;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserAppService {

    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private String getAuditor() {
        return SecurityContextHolder.getContext()
                .getAuthentication()
                .getName(); // returns email from jwt
    }

    private void checkOwnership(UUID uuid) {

    }

    private User findUserById(UUID uuid) {
        return userRepository.findById(uuid).orElseThrow(
                () -> new ResourceNotFoundException("User", "ID", uuid.toString())

        );
    }

    private void assertEmailNotTaken(String email){
        boolean isEmailActive = userRepository.existsByEmailAndDeletedFalse(email);
        if (isEmailActive) {
            throw new ResourceAlreadyExistsException("User", "Email", email);
        }
    }

    @Transactional(readOnly = true)
    public UserDto findUserDtoById(UUID uuid) {
        User user = findUserById(uuid);
        return userMapper.toResponse(user);
    }

    @Transactional(readOnly = true)
    public PaginatedResponseDto<UserDto> findAllUsers(int page, int size) {
        PaginatedResponseDto<User> users = userRepository.findAllUsers(page, size);

        List<UserDto> userDtos = users.data()
                .stream()
                .map(userMapper::toResponse)
                .toList();

        return new PaginatedResponseDto<>(
                userDtos,
                users.totalElements(),
                users.totalPages(),
                users.currentPage()
        );
    }

    @Transactional(readOnly = true)
    public UserDto findUserDtoByEmail(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new ResourceNotFoundException("User", "Email", email)

        );
        return userMapper.toResponse(user);
    }


    @Transactional
    public void deleteUser(UUID uuid) {
        User user = findUserById(uuid);
        user.deleteUser("self"); // change after security context
        userRepository.saveUser(user);
        log.info("User with the id successfully deleted: {}", uuid);
    }

    @Transactional
    public void updateFullName(UUID uuid, UpdateFullNameDto requestDto){
        User user = findUserById(uuid);
        user.updateFullName(new FullName(requestDto.firstName(), requestDto.lastName()),"self");

        userRepository.saveUser(user);

        log.info("Full name updated successfully, user id = {} ", uuid);

    }

    @Transactional
    public void updateEmail(UUID uuid, UpdateEmailDto requestDto){
        User user = findUserById(uuid);
        assertEmailNotTaken(requestDto.email());

        user.updateEmail(new Email(requestDto.email()), "self");

        userRepository.saveUser(user);

        log.info("Email updated successfully, user id = {} ", uuid);
    }

    @Transactional
    public void updateAge(UUID uuid, UpdateAgeDto requestDto){
        User user = findUserById(uuid);
        user.updateAge(new Age(requestDto.age()), "self");

        userRepository.saveUser(user);

        log.info("Age updated successfully, user id = {} ", uuid);
    }
}

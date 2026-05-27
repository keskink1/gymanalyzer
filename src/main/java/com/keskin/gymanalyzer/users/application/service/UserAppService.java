package com.keskin.gymanalyzer.users.application.service;

import com.keskin.gymanalyzer.common.dto.PaginatedResponseDto;
import com.keskin.gymanalyzer.common.exception.ForbiddenException;
import com.keskin.gymanalyzer.common.exception.ResourceAlreadyExistsException;
import com.keskin.gymanalyzer.common.exception.ResourceNotFoundException;
import com.keskin.gymanalyzer.users.application.dto.*;
import com.keskin.gymanalyzer.users.application.mapper.UserMapper;
import com.keskin.gymanalyzer.users.domain.model.User;
import com.keskin.gymanalyzer.users.domain.repository.UserRepository;
import com.keskin.gymanalyzer.users.domain.valueobject.Age;
import com.keskin.gymanalyzer.users.domain.valueobject.Email;
import com.keskin.gymanalyzer.users.domain.valueobject.FullName;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

import static com.keskin.gymanalyzer.common.utils.SecurityUtils.getCurrentUserEmail;
import static com.keskin.gymanalyzer.common.utils.SecurityUtils.isAdmin;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserAppService {

    private final UserMapper userMapper;
    private final UserRepository userRepository;

    private void checkOwnership(User user) {
        if (!isAdmin() && !user.getEmail().getValue().equals(getCurrentUserEmail())) {
            throw new ForbiddenException("No access");
        }
    }

    private User findUserAndVerifyAccess(UUID uuid) {
        User user = findUserById(uuid);
        checkOwnership(user);
        return user;
    }

    private User findUserById(UUID uuid) {
        return userRepository.findById(uuid).orElseThrow(
                () -> new ResourceNotFoundException("User", "ID", uuid.toString())
        );
    }

    private void assertEmailNotTaken(String email) {
        if (userRepository.existsByEmailAndDeletedFalse(email)) {
            throw new ResourceAlreadyExistsException("User", "Email", email);
        }
    }

    @Transactional(readOnly = true)
    public UserDto findUserDtoById(UUID uuid) {
        User user = findUserAndVerifyAccess(uuid);
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
        checkOwnership(user);
        return userMapper.toResponse(user);
    }

    @Transactional
    public void deleteUser(UUID uuid) {
        User user = findUserAndVerifyAccess(uuid);
        user.deleteUser(getCurrentUserEmail());

        userRepository.saveUser(user);
        log.info("User deleted successfully, userId={}", uuid);
    }

    @Transactional
    public void updateFullName(UUID uuid, UpdateFullNameDto requestDto) {
        User user = findUserAndVerifyAccess(uuid);

        user.updateFullName(new FullName(requestDto.firstName(), requestDto.lastName()), getCurrentUserEmail());
        userRepository.saveUser(user);
        log.info("Full name updated successfully, userId={}", uuid);
    }

    @Transactional
    public void updateEmail(UUID uuid, UpdateEmailDto requestDto) {
        User user = findUserAndVerifyAccess(uuid);
        assertEmailNotTaken(requestDto.email());
        user.updateEmail(new Email(requestDto.email()), getCurrentUserEmail());

        userRepository.saveUser(user);
        log.info("Email updated successfully, userId={}", uuid);
    }

    @Transactional
    public void updateAge(UUID uuid, UpdateAgeDto requestDto) {
        User user = findUserAndVerifyAccess(uuid);

        user.updateAge(new Age(requestDto.age()), getCurrentUserEmail());
        userRepository.saveUser(user);
        log.info("Age updated successfully, userId={}", uuid);
    }
}
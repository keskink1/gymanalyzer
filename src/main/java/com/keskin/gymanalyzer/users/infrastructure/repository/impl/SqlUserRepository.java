package com.keskin.gymanalyzer.users.infrastructure.repository.impl;

import com.keskin.gymanalyzer.common.dto.PaginatedResponseDto;
import com.keskin.gymanalyzer.users.domain.model.User;
import com.keskin.gymanalyzer.users.domain.repository.UserRepository;
import com.keskin.gymanalyzer.users.infrastructure.entity.UserEntity;
import com.keskin.gymanalyzer.users.infrastructure.mapper.UserPersistenceMapper;
import com.keskin.gymanalyzer.users.infrastructure.repository.JpaUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class SqlUserRepository implements UserRepository {

    private final JpaUserRepository jpaUserRepository;
    private final UserPersistenceMapper userMapper;

    @Override
    public PaginatedResponseDto<User> findAllUsers(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<UserEntity> usersPage = jpaUserRepository.findAll(pageable);

        List<User> users = usersPage.getContent()
                .stream()
                .map(user -> userMapper.toDomain(user))
                .toList();

        return new PaginatedResponseDto<>(
                users,
                usersPage.getTotalElements(),
                usersPage.getTotalPages(),
                usersPage.getNumber()
        );
    }

    @Override
    public Optional<User> findById(UUID uuid) {
        return jpaUserRepository.findById(uuid)
                .map(user -> userMapper.toDomain(user));
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return jpaUserRepository.findByEmail(email)
                .map(user -> userMapper.toDomain(user));
    }

    @Override
    public void saveUser(User user) {
        UserEntity entity = userMapper.toEntity(user);
        jpaUserRepository.save(entity);
    }

    @Override
    public boolean existsByEmailAndDeletedFalse(String email) {
        return jpaUserRepository.existsByEmailAndDeletedFalse(email);
    }
}

package com.keskin.gymanalyzer.users.infrastructure.mapper;

import com.keskin.gymanalyzer.users.domain.model.User;
import com.keskin.gymanalyzer.users.domain.valueobject.Age;
import com.keskin.gymanalyzer.users.domain.valueobject.Email;
import com.keskin.gymanalyzer.users.domain.valueobject.FullName;
import com.keskin.gymanalyzer.users.domain.valueobject.Password;
import com.keskin.gymanalyzer.users.infrastructure.entity.UserEntity;
import org.springframework.stereotype.Component;

@Component
public class UserPersistenceMapper {

    public UserEntity toEntity(User domain) {
        UserEntity entity = new UserEntity();
        entity.setUuid(domain.getUuid());
        entity.setFirstName(domain.getFullName().firstName());
        entity.setLastName(domain.getFullName().lastName());
        entity.setEmail(domain.getEmail().getValue());
        entity.setPassword(domain.getPassword().getValue());
        entity.setAge(domain.getAge().value());
        entity.setRole(domain.getRole());
        entity.setCreatedAt(domain.getCreatedAt());
        entity.setCreatedBy(domain.getCreatedBy());
        entity.setUpdatedAt(domain.getUpdatedAt());
        entity.setUpdatedBy(domain.getUpdatedBy());
        entity.setDeleted(domain.isDeleted());
        entity.setDeletedAt(domain.getDeletedAt());
        entity.setDeletedBy(domain.getDeletedBy());
        return entity;
    }

    public User toDomain(UserEntity entity) {
        return new User(
                entity.getUuid(),
                entity.getCreatedAt(),
                entity.getCreatedBy(),
                entity.isDeleted(),
                entity.getDeletedAt(),
                entity.getDeletedBy(),
                entity.getUpdatedAt(),
                entity.getUpdatedBy(),
                new FullName(entity.getFirstName(), entity.getLastName()),
                Password.hashed(entity.getPassword()),
                new Email(entity.getEmail()),
                new Age(entity.getAge()),
                entity.getRole()
        );
    }
}
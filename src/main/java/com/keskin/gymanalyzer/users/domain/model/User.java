package com.keskin.gymanalyzer.users.domain.model;

import com.keskin.gymanalyzer.common.exception.InvalidValidationException;
import com.keskin.gymanalyzer.common.model.BaseEntity;
import com.keskin.gymanalyzer.users.domain.valueobject.Age;
import com.keskin.gymanalyzer.users.domain.valueobject.Email;
import com.keskin.gymanalyzer.users.domain.valueobject.FullName;
import com.keskin.gymanalyzer.users.domain.valueobject.Password;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;


@Getter
public class User extends BaseEntity {
    private FullName fullName;

    private Password password;

    private Email email;

    private Age age;

    private Role role;

    public User(UUID uuid, LocalDateTime createdAt, String createdBy, boolean deleted, LocalDateTime deletedAt, String deletedBy, LocalDateTime updatedAt, String updatedBy, FullName fullName, Password password, Email email, Age age, Role role) {
        super(uuid, createdAt, createdBy, deleted, deletedAt, deletedBy, updatedAt, updatedBy);
        this.fullName = fullName;
        this.password = password;
        this.email = email;
        this.age = age;
        this.role = role;
    }

    public static User createUser(String createdBy, FullName fullName, Age age, Email email, Password password) {
        return new User(createdBy, fullName, age, email, password);
    }

    // private, unmodifiable constructor
    private User(String createdBy, FullName fullName, Age age, Email email, Password password) {
        super(createdBy);
        this.fullName = fullName;
        this.password = password;
        this.email = email;
        this.age = age;
        this.role = Role.USER;
    }

    public void promoteToAdmin(String auditor) {
        if (this.role == Role.ADMIN) {
            return;
        }

        role = Role.ADMIN;
    }

    public void deleteUser(String auditor) {
        if (this.role == Role.ADMIN){
             throw new InvalidValidationException("Admins can't be deleted!");
        }
    }

    public void updateFullName(FullName newFullName, String auditor) {
        this.fullName = newFullName;
    }

    public void updateAge(Age newAge, String auditor) {
        this.age = newAge;
    }

    public void updateEmail(Email newEmail, String auditor) {
        this.email = newEmail;
    }

    public void changePassword(Password newPassword, String auditor){
        if (newPassword.equals(this.password)){
            throw new InvalidValidationException("New password can't be same with old password");
        }
        this.password = newPassword;
    }
}

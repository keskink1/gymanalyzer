package com.keskin.gymanalyzer.users;

import com.keskin.gymanalyzer.common.exception.InvalidValidationException;
import com.keskin.gymanalyzer.users.domain.model.User;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class UserEntityTest {

    @Test
    void shouldThrowExceptionWhenAge_isLessThan18() {
        assertThrows(InvalidValidationException.class, () ->
                new User(UUID.randomUUID(), new Name("John Doe"), new Email("john@gmail.com"),
                        new Password("Pass123!"), new Age(16))
        );
    }

    @Test
    void shouldThrowExceptionWhenEmailFormat_isWrong() {
        assertThrows(InvalidValidationException.class, () ->
                new User(UUID.randomUUID(), new Name("John Doe"), new Email("johngmail.com"),
                        new Password("Pass123!"), new Age(22))
        );
    }

    @Test
    void shouldThrowExceptionWhenName_isNull() {
        assertThrows(InvalidValidationException.class, () ->
                new User(UUID.randomUUID(), new Name(null), new Email("john@gmail.com"),
                        new Password("Pass123!"), new Age(22))
        );
    }

    @Test
    void shouldCreateUserSuccessfully() {
        UUID id = UUID.randomUUID();
        Name name = new Name("John Doe");
        Email email = new Email("johndoe@gmail.com");
        Password password = new Password("Pass123!");
        Age age = new Age(25);

        User user = new User(id, name, email, password, age);

        assertNotNull(user);
        assertEquals(name, user.getName());
        assertEquals(email, user.getEmail());
        assertEquals(age, user.getAge());
        assertFalse(user.isDeleted());
        assertNull(user.getDeletedBy());
        assertNull(user.getDeletedAt());
    }

    @Test
    void shouldThrowWhenDeletingAdmin() {
        User admin = new User(UUID.randomUUID(), new Name("Admin"), new Email("admin@gmail.com"),
                new Password("Pass123!"), new Age(30));
        admin.promoteToAdmin("system");

        assertThrows(IllegalStateException.class, () -> admin.deleteUser("system"));
    }
}
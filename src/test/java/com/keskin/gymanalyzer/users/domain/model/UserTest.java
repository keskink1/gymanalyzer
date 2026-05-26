package com.keskin.gymanalyzer.users.domain.model;

import com.keskin.gymanalyzer.common.exception.InvalidValidationException;
import com.keskin.gymanalyzer.users.domain.valueobject.Age;
import com.keskin.gymanalyzer.users.domain.valueobject.Email;
import com.keskin.gymanalyzer.users.domain.valueobject.FullName;
import com.keskin.gymanalyzer.users.domain.valueobject.Password;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UserTest {

    @Test
    void shouldThrowExceptionWhenAge_isLessThan18() {
        assertThrows(InvalidValidationException.class, () ->
                User.createUser(
                        "system",
                        new FullName("John", "Doe"),
                        new Age(15),
                        new Email("johndoe@gmail.com"),
                        Password.rawValidated("Pass123!")
                )
        );
    }

    @Test
    void shouldThrowExceptionWhenEmailFormat_isWrong() {
        assertThrows(InvalidValidationException.class, () ->
                User.createUser(
                        "system",
                        new FullName("John", "Doe"),
                        new Age(22),
                        new Email("johndoe"),
                        Password.rawValidated("Pass123!")
                )
        );
    }

    @Test
    void shouldThrowExceptionWhenFullName_isNull() {
        assertThrows(InvalidValidationException.class, () ->
                User.createUser(
                        "system",
                        new FullName(null, "Doe"),
                        new Age(22),
                        new Email("johndoe@gmail.com"),
                        Password.rawValidated("Pass123!")
                )
        );
    }

    @Test
    void shouldCreateUserSuccessfully() {
        FullName fullName = new FullName("John", "Doe");
        Email email = new Email("johndoe@gmail.com");
        Age age = new Age(25);
        Password password = Password.rawValidated("Pass123!");

        User user = User.createUser(
                "system",
                fullName,
                age,
                email,
                password
        );

        assertNotNull(user);
        assertEquals(fullName, user.getFullName());
        assertEquals(email, user.getEmail());
        assertEquals(age, user.getAge());
        assertFalse(user.isDeleted());
        assertNull(user.getDeletedBy());
        assertNull(user.getDeletedAt());
    }

    @Test
    void shouldThrowWhenDeletingAdmin() {
        User admin = User.createUser(
                "system",
                new FullName("Admin", "User"),
                new Age(22),
                new Email("admin@gmail.com"),
                Password.rawValidated("John123!*")
        );

        admin.promoteToAdmin("system");

        assertThrows(InvalidValidationException.class, () ->
                admin.deleteUser("system")
        );
    }
}
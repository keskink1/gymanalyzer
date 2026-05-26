package com.keskin.gymanalyzer.users.domain.valueobject;

import com.keskin.gymanalyzer.common.exception.InvalidValidationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;


public class EmailTest {

    @ParameterizedTest
    @ValueSource(strings = {
            "notanemail",
            "missing@domain",
            "@nodomain.com",
            "spaces in@email.com"
    })
    void shouldThrowWhenEmailFormatIsInvalid(String invalidEmail) {
        assertThrows(InvalidValidationException.class, () -> new Email(invalidEmail));
    }

    @Test
    void shouldThrowWhenEmailIsNull() {
        assertThrows(InvalidValidationException.class, () -> new Email(null));
    }

    @Test
    void shouldNormalizeToLowercase() {
        Email email = new Email("USER@EMAIL.COM");
        assertEquals("user@email.com", email.getValue());
    }

    @Test
    void shouldTrimWhitespace() {
        Email email = new Email("  user@email.com  ");
        assertEquals("user@email.com", email.getValue());
    }

    @Test
    void shouldBeEqualWhenSameEmailDifferentCase() {
        Email e1 = new Email("user@email.com");
        Email e2 = new Email("USER@EMAIL.COM");
        assertEquals(e1, e2);
    }
}

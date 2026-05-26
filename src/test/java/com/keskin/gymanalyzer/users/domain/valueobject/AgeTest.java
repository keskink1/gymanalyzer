package com.keskin.gymanalyzer.users.domain.valueobject;

import com.keskin.gymanalyzer.common.exception.InvalidValidationException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AgeTest {
    @Test
    void shouldThrowWhenAgeIsNull(){
        assertThrows(InvalidValidationException.class, () -> new Age(null));
    }

    @Test
    void shouldThrowWhenAgeUnder18(){
        assertThrows(InvalidValidationException.class, () -> new Age(12));
    }

    @Test
    void shouldCreateSuccessfully_whenAgeIsValid() {
        Age age = new Age(25);
        assertEquals(25, age.value());
    }

    @Test
    void shouldCreateSuccessfully_whenAgeIsExactly18() {
        Age age = new Age(18);
        assertEquals(18, age.value());
    }
}

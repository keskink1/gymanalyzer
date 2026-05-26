package com.keskin.gymanalyzer.users.domain.valueobject;

import com.keskin.gymanalyzer.common.exception.InvalidValidationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

public class FullNameTest {

    @ParameterizedTest
    @ValueSource(strings = {
            "a",
            "a  "
    })    void shouldThrowExceptionWhenFirstName_isTooShort(String shortFirstName) {
        assertThrows(InvalidValidationException.class, () -> new FullName(shortFirstName, "allen"));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "a",
            "x   ",
    })    void shouldThrowExceptionWhenLastName_isTooShort(String shortLastName) {
        assertThrows(InvalidValidationException.class, () -> new FullName("john", shortLastName));
    }

    // don't test the spaces for the between name because people might have 2 names or surnames
    @Test
    void shouldTrimWhiteSpace(){
        FullName dirtyFullName = new FullName("  john    ", "  doe  ");
        FullName expectedCleanFullName = new FullName("john", "doe");

        assertEquals(expectedCleanFullName, dirtyFullName);
    }

    @Test
    void shouldThrowWhenFirstNameIsNull() {
        assertThrows(InvalidValidationException.class, () -> new FullName(null, "Doe"));
    }

    @Test
    void shouldThrowWhenLastNameIsBlank() {
        assertThrows(InvalidValidationException.class, () -> new FullName("John", "  "));
    }
}

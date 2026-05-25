package com.keskin.gymanalyzer.users.domain.valueobject;

import com.keskin.gymanalyzer.common.exception.InvalidValidationException;
import lombok.Getter;

import java.util.regex.Pattern;

@Getter
public class Email {
    private static final Pattern PATTERN =
            Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9-]+\\.[A-Za-z]{2,}$");

    private final String value;

    public Email(String email) {
        validate(email);
        this.value = email.toLowerCase().trim();
    }

    private void validate(String email) {
        if (email == null || email.isBlank())
            throw new InvalidValidationException("Email can't be null or blank.");
        if (!PATTERN.matcher(email.trim()).matches())
            throw new InvalidValidationException("Invalid email format.");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Email other)) return false;
        return value.equalsIgnoreCase(other.value);
    }

    @Override
    public int hashCode() {
        return value.toLowerCase().hashCode();
    }
}
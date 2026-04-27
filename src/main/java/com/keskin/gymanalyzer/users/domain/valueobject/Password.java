package com.keskin.gymanalyzer.users.domain.valueobject;

import com.keskin.gymanalyzer.common.exception.InvalidValidationException;
import lombok.Getter;

import java.util.Objects;
import java.util.regex.Pattern;

@Getter
public class Password {
    private final String value;

    private static final String REGEX = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{6,}$";
    private static final Pattern PATTERN = Pattern.compile(REGEX);

    public Password(String value) {
        if (value == null || !isValid(value)) {
            throw new InvalidValidationException("Password must be at least 6 characters, containing 1 capital letter, 1 small letter, 1 number and 1 special character.");
        }
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Password password = (Password) o;
        return Objects.equals(value, password.value);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }

    private boolean isValid(String value) {
        return PATTERN.matcher(value).matches();
    }

    @Override
    public String toString() {
        return "*******";
    }
}
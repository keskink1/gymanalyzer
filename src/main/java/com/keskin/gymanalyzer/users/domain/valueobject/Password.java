package com.keskin.gymanalyzer.users.domain.valueobject;

import com.keskin.gymanalyzer.common.exception.InvalidValidationException;
import lombok.Getter;

import java.util.Objects;
import java.util.regex.Pattern;

@Getter
public class Password {

    private final String value;
    private final boolean hashed;

    private static final String REGEX =
            "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{6,}$";

    private static final Pattern PATTERN = Pattern.compile(REGEX);

    // raw password constructor. only for validation
    private Password(String value, boolean hashed) {
        this.value = value;
        this.hashed = hashed;
    }

    public static Password rawValidated(String value) {
        if (value == null || !PATTERN.matcher(value).matches()) {
            throw new InvalidValidationException(
                    "Password must be at least 6 characters, contain uppercase, lowercase, number and special char."
            );
        }
        return new Password(value, false);
    }

    // hashed password, no validation
    public static Password hashed(String value) {
        return new Password(value, true);
    }

    public boolean isHashed() {
        return hashed;
    }

    @Override
    public String toString() {
        return "*******";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Password)) return false;
        Password password = (Password) o;
        return Objects.equals(value, password.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
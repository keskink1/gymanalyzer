package com.keskin.gymanalyzer.users.domain.valueobject;

import com.keskin.gymanalyzer.common.exception.InvalidValidationException;

import java.util.Objects;

public class FullName {
    private final String firstName;
    private final String lastName;

    public FullName(String firstName, String lastName) {
        String trimmedFirst = firstName == null ? null : firstName.trim();
        String trimmedLast = lastName == null ? null : lastName.trim();

        validate(trimmedFirst, trimmedLast);

        this.firstName = trimmedFirst;
        this.lastName = trimmedLast;
    }

    private void validate(String firstName, String lastName) {
        if (firstName == null || firstName.isBlank())
            throw new InvalidValidationException("First name cannot be null or blank.");
        if (lastName == null || lastName.isBlank())
            throw new InvalidValidationException("Last name cannot be null or blank.");
        if (firstName.length() < 2)
            throw new InvalidValidationException("First name is too short.");
        if (lastName.length() < 2)
            throw new InvalidValidationException("Last name is too short.");
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        FullName fullName = (FullName) o;
        return Objects.equals(firstName, fullName.firstName) && Objects.equals(lastName, fullName.lastName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastName);
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getDisplayName() {
        return firstName + " " + lastName;
    }
}

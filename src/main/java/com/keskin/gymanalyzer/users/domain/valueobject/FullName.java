package com.keskin.gymanalyzer.users.domain.valueobject;

import com.keskin.gymanalyzer.common.exception.InvalidValidationException;

import java.util.Objects;

public record FullName (
        String firstName,
        String lastName
){

    public FullName(String firstName, String lastName) {
        validate(firstName,lastName);
        this.firstName = firstName.trim();
        this.lastName = lastName.trim();
    }

    private void validate(String firstName, String lastName) {
        if (firstName == null || firstName.isBlank())
            throw new InvalidValidationException("First name cannot be null or blank.");
        if (lastName == null || lastName.isBlank())
            throw new InvalidValidationException("Last name cannot be null or blank.");
        if (firstName.length() < 3)
            throw new InvalidValidationException("First name is too short.");
        if (lastName.length() < 3)
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

    public String getDisplayName() {
        return firstName + " " + lastName;
    }
}

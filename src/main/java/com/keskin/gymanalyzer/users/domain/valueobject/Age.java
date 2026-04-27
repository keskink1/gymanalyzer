package com.keskin.gymanalyzer.users.domain.valueobject;

import com.keskin.gymanalyzer.common.exception.InvalidValidationException;

import java.util.Objects;

public record Age(
        Integer age
) {

    public Age(Integer age) {
        validate(age);
        this.age = age;
    }

    private void validate(Integer age){
        if (age == null) throw new InvalidValidationException("Age cannot be null.");
        if (age < 18) throw new InvalidValidationException("Age must be higher than 18.");
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Age age1 = (Age) o;
        return Objects.equals(age, age1.age);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(age);
    }
}

package com.rfsystems.subscription.domain.person;

import com.rfsystems.subscription.domain.ValueObject;

import java.util.regex.Pattern;

public record Email(String value) implements ValueObject {

    private static final Pattern VALIDATOR = Pattern.compile("^(.+)@(\\S+)$");

    public Email {
        this.assertArgumentNotEmpty(value, "'email' should not be empty");
        this.assertConditionTrue(VALIDATOR.matcher(value).matches(), "'email' is invalid");
    }
}

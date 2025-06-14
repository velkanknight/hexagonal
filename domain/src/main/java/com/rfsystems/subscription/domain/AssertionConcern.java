package com.rfsystems.subscription.domain;

import com.rfsystems.subscription.domain.exceptions.DomainException;

public interface AssertionConcern {

    default <T> T assertArgumentNotNull(T val, String aMessage) {
        if (val == null) {
            throw DomainException.with(aMessage);
        }
        return val;
    }

    default String assertArgumentNotEmpty(String val, String aMessage) {
        if (val == null || val.isBlank()) {
            throw DomainException.with(aMessage);
        }
        return val;
    }

    default String assertArgumentLength(String val, int length, String aMessage) {
        if (val == null || val.length() != length) {
            throw DomainException.with(aMessage);
        }
        return val;
    }

    default void assertConditionTrue(Boolean val, String aMessage) {
        if (Boolean.FALSE.equals(val)) {
            throw DomainException.with(aMessage);
        }
    }

    default String assertArgumentMaxLength(String val, int length, String aMessage) {
        if (val != null && val.length() > length) {
            throw DomainException.with(aMessage);
        }
        return aMessage;
    }
}

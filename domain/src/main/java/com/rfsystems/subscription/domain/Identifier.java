package com.rfsystems.subscription.domain;

public interface Identifier<T> extends ValueObject {
    T value();
}

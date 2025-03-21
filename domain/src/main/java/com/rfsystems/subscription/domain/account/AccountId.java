package com.rfsystems.subscription.domain.account;

import com.rfsystems.subscription.domain.Identifier;

public record AccountId(String value) implements Identifier<String> {

    public AccountId {
        this.assertArgumentNotEmpty(value, "'accountId' should not be empty");
    }
}

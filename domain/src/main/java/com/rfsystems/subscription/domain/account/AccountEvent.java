package com.rfsystems.subscription.domain.account;

import com.rfsystems.subscription.domain.DomainEvent;

public sealed interface AccountEvent extends DomainEvent permits AccountCreated {

    String TYPE = "Account";

    String accountId();

    @Override
    default String aggregateId() {
        return accountId();
    }

    @Override
    default String aggregateType() {
        return TYPE;
    }

}

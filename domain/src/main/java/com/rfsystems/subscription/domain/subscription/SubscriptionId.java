package com.rfsystems.subscription.domain.subscription;

import com.rfsystems.subscription.domain.Identifier;

public record SubscriptionId(String value) implements Identifier<String> {

    public SubscriptionId {
        this.assertArgumentNotEmpty(value, "'subscriptionId' should not be empty");
    }
}

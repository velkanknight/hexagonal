package com.rfsystems.subscription.application.subscription.impl;

import com.rfsystems.subscription.application.subscription.CancelSubscription;
import com.rfsystems.subscription.domain.account.AccountId;
import com.rfsystems.subscription.domain.exceptions.DomainException;
import com.rfsystems.subscription.domain.subscription.Subscription;
import com.rfsystems.subscription.domain.subscription.SubscriptionCommand;
import com.rfsystems.subscription.domain.subscription.SubscriptionGateway;
import com.rfsystems.subscription.domain.subscription.SubscriptionId;

import java.util.Objects;

public class DefaultCancelSubscription extends CancelSubscription {

    private final SubscriptionGateway subscriptionGateway;

    public DefaultCancelSubscription(final SubscriptionGateway subscriptionGateway) {
        this.subscriptionGateway = Objects.requireNonNull(subscriptionGateway);
    }

    @Override
    public CancelSubscription.Output execute(final CancelSubscription.Input in) {
        if (in == null) {
            throw new IllegalArgumentException("Input of DefaultCancelSubscription cannot be null");
        }

        final var aSubscription = this.subscriptionGateway.latestSubscriptionOfAccount(new AccountId(in.accountId()))
                .filter(it -> it.accountId().equals(new AccountId(in.accountId())))
                .orElseThrow(() -> DomainException.with("Subscription for account %s was not found".formatted(in.accountId())));

        aSubscription.execute(new SubscriptionCommand.CancelSubscription());

        this.subscriptionGateway.save(aSubscription);
        return new StdOutput(aSubscription);
    }

    record StdOutput(SubscriptionId subscriptionId, String subscriptionStatus) implements CancelSubscription.Output {
        public StdOutput(Subscription aSubscription) {
            this(aSubscription.id(), aSubscription.status().value());
        }
    }
}

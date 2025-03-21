package com.rfsystems.subscription.domain.subscription;

import com.rfsystems.subscription.domain.DomainEvent;
import com.rfsystems.subscription.domain.plan.Plan;
import com.rfsystems.subscription.domain.utils.InstantUtils;

import java.time.Instant;
import java.time.LocalDate;

public sealed interface SubscriptionEvent extends DomainEvent
        permits SubscriptionCanceled, SubscriptionCreated, SubscriptionRenewed, SubscriptionIncomplete {

    String TYPE = "Subscription";

    String subscriptionId();

    @Override
    default String aggregateId() {
        return subscriptionId();
    }

    @Override
    default String aggregateType() {
        return TYPE;
    }
}

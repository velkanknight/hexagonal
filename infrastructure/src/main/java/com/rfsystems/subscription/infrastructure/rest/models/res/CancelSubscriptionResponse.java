package com.rfsystems.subscription.infrastructure.rest.models.res;

import com.rfsystems.subscription.application.subscription.CancelSubscription;
import com.rfsystems.subscription.domain.AssertionConcern;

public record CancelSubscriptionResponse(String subscriptionId, String subscriptionStatus) implements AssertionConcern {

    public CancelSubscriptionResponse {
        this.assertArgumentNotEmpty(subscriptionId, "CancelSubscriptionResponse 'subscriptionId' should not be empty");
        this.assertArgumentNotEmpty(subscriptionStatus, "CancelSubscriptionResponse 'subscriptionStatus' should not be empty");
    }

    public CancelSubscriptionResponse(CancelSubscription.Output out) {
        this(out.subscriptionId().value(), out.subscriptionStatus());
    }
}

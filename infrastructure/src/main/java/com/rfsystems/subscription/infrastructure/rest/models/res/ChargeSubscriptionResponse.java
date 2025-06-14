package com.rfsystems.subscription.infrastructure.rest.models.res;

import com.rfsystems.subscription.application.subscription.ChargeSubscription;

public record ChargeSubscriptionResponse(
        String subscriptionId,
        String subscriptionStatus,
        String subscriptionDueDate,
        String paymentTransactionId,
        String paymentTransactionError
) {

    public ChargeSubscriptionResponse(ChargeSubscription.Output out) {
        this(
                out.subscriptionId().value(),
                out.subscriptionStatus(),
                out.subscriptionDueDate().toString(),
                out.paymentTransaction() != null ? out.paymentTransaction().transactionId() : "",
                out.paymentTransaction() != null ? out.paymentTransaction().errorMessage() : ""
        );
    }
}

package com.rfsystems.subscription.application.subscription;

import com.rfsystems.subscription.application.UseCase;
import com.rfsystems.subscription.domain.subscription.SubscriptionId;

public abstract class CancelSubscription extends UseCase<CancelSubscription.Input, CancelSubscription.Output> {

    public interface Input {
        String accountId();
    }

    public interface Output {
        String subscriptionStatus();
        SubscriptionId subscriptionId();
    }
}

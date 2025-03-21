package com.rfsystems.subscription.application.subscription;

import com.rfsystems.subscription.application.UseCase;
import com.rfsystems.subscription.domain.subscription.SubscriptionId;

public abstract class CreateSubscription extends UseCase<CreateSubscription.Input, CreateSubscription.Output> {

    public interface Input {
        String accountId();
        Long planId();
    }

    public interface Output {
        SubscriptionId subscriptionId();
    }
}

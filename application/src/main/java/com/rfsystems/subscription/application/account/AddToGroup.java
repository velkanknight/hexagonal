package com.rfsystems.subscription.application.account;

import com.rfsystems.subscription.application.UseCase;
import com.rfsystems.subscription.domain.subscription.SubscriptionId;

public abstract class AddToGroup extends UseCase<AddToGroup.Input, AddToGroup.Output> {

    public interface Input {
        String accountId();
        String subscriptionId();
        String groupId();
    }

    public interface Output {
        SubscriptionId subscriptionId();
    }
}

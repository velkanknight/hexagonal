package com.rfsystems.subscription.domain.subscription.status;

import com.rfsystems.subscription.domain.payment.exceptions.DomainException;
import com.rfsystems.subscription.domain.subscription.Subscription;
import com.rfsystems.subscription.domain.subscription.SubscriptionCommand.ChangeStatus;

public record IncompleteSubscriptionStatus(Subscription subscription) implements SubscriptionStatus {

    @Override
    public void trailing() {
        throw DomainException.with("Subscription with status incomplete can´t transit to trailing");
    }

    @Override
    public void incomplete() {

    }

    @Override
    public void active() {
        this.subscription.execute(new ChangeStatus(ACTIVE));
    }

    @Override
    public void cancel() {
        this.subscription.execute(new ChangeStatus(CANCELED));
    }

    @Override
    public boolean equals(Object obj) {
        return obj != null && obj.getClass().equals(getClass());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return value();
    }
}

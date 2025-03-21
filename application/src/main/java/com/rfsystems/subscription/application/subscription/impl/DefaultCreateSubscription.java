package com.rfsystems.subscription.application.subscription.impl;

import com.rfsystems.subscription.application.subscription.CreateSubscription;
import com.rfsystems.subscription.domain.account.Account;
import com.rfsystems.subscription.domain.account.AccountGateway;
import com.rfsystems.subscription.domain.account.AccountId;
import com.rfsystems.subscription.domain.exceptions.DomainException;
import com.rfsystems.subscription.domain.plan.Plan;
import com.rfsystems.subscription.domain.plan.PlanGateway;
import com.rfsystems.subscription.domain.plan.PlanId;
import com.rfsystems.subscription.domain.subscription.Subscription;
import com.rfsystems.subscription.domain.subscription.SubscriptionGateway;
import com.rfsystems.subscription.domain.subscription.SubscriptionId;

public class DefaultCreateSubscription extends CreateSubscription {

    private final AccountGateway accountGateway;
    private final PlanGateway planGateway;
    private final SubscriptionGateway subscriptionGateway;

    public DefaultCreateSubscription(
            final AccountGateway accountGateway,
            final PlanGateway planGateway,
            final SubscriptionGateway subscriptionGateway
    ) {
        this.accountGateway = accountGateway;
        this.planGateway = planGateway;
        this.subscriptionGateway = subscriptionGateway;
    }

    @Override
    public CreateSubscription.Output execute(final CreateSubscription.Input in) {
        if (in == null) {
            throw new IllegalArgumentException("Input of DefaultCreateSubscription should not be null");
        }

        final var accountId = new AccountId(in.accountId());
        final var planId = new PlanId(in.planId());

        validateActiveSubscription(in, accountId);

        final var aPlan = this.planGateway.planOfId(planId)
                .filter(Plan::active)
                .orElseThrow(() -> DomainException.notFound(Plan.class, planId));

        final var anUserAccount = this.accountGateway.accountOfId(accountId)
                .orElseThrow(() -> DomainException.notFound(Account.class, accountId));

        final var aNewSubscription = this.newSubscriptionWith(anUserAccount, aPlan);
        this.subscriptionGateway.save(aNewSubscription);
        return new StdOutput(aNewSubscription.id());
    }

    private Subscription newSubscriptionWith(final Account anUserAccount, final Plan aPlan) {
        return Subscription.newSubscription(
                this.subscriptionGateway.nextId(),
                anUserAccount.id(),
                aPlan
        );
    }

    private void validateActiveSubscription(Input in, AccountId accountId) {
        this.subscriptionGateway.latestSubscriptionOfAccount(accountId).ifPresent(sub -> {
            if (!sub.isCanceled()) {
                throw DomainException.with("Account %s already has a active subscription".formatted(in.accountId()));
            }
        });
    }

    public record StdOutput(SubscriptionId subscriptionId) implements CreateSubscription.Output {

    }
}

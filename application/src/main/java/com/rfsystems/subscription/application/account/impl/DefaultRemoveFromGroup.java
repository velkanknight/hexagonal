package com.rfsystems.subscription.application.account.impl;

import com.rfsystems.subscription.application.account.RemoveFromGroup;
import com.rfsystems.subscription.domain.AggregateRoot;
import com.rfsystems.subscription.domain.Identifier;
import com.rfsystems.subscription.domain.account.Account;
import com.rfsystems.subscription.domain.account.AccountGateway;
import com.rfsystems.subscription.domain.account.AccountId;
import com.rfsystems.subscription.domain.account.idp.GroupId;
import com.rfsystems.subscription.domain.account.idp.IdentityProviderGateway;
import com.rfsystems.subscription.domain.payment.exceptions.DomainException;
import com.rfsystems.subscription.domain.subscription.Subscription;
import com.rfsystems.subscription.domain.subscription.SubscriptionGateway;
import com.rfsystems.subscription.domain.subscription.SubscriptionId;

import java.time.LocalDate;
import java.util.Objects;

public class DefaultRemoveFromGroup extends RemoveFromGroup {

    private final AccountGateway accountGateway;
    private final IdentityProviderGateway identityProviderGateway;
    private final SubscriptionGateway subscriptionGateway;

    public DefaultRemoveFromGroup(
            final AccountGateway accountGateway,
            final IdentityProviderGateway identityProviderGateway,
            final SubscriptionGateway subscriptionGateway
    ) {
        this.accountGateway = Objects.requireNonNull(accountGateway);
        this.identityProviderGateway = Objects.requireNonNull(identityProviderGateway);
        this.subscriptionGateway = Objects.requireNonNull(subscriptionGateway);
    }

    @Override
    public Output execute(final RemoveFromGroup.Input in) {
        if (in == null) {
            throw new IllegalArgumentException("Input to DefaultRemoveFromGroup cannot be null");
        }

        final var anAccountId = new AccountId(in.accountId());
        final var aSubscriptionId = new SubscriptionId(in.subscriptionId());

        final var aSubscription = subscriptionGateway.subscriptionOfId(aSubscriptionId)
                .filter(it -> it.accountId().equals(anAccountId))
                .orElseThrow(() -> notFound(Subscription.class, aSubscriptionId));

        if (isRemovableStatus(aSubscription) && isExpired(aSubscription)) {
            final var userId = this.accountGateway.accountOfId(anAccountId)
                    .orElseThrow(() -> notFound(Account.class, anAccountId))
                    .userId();

            this.identityProviderGateway.removeUserFromGroup(userId, new GroupId(in.groupId()));
        }

        return new StdOutput(aSubscriptionId);
    }

    private static boolean isExpired(final Subscription aSubscription) {
        return aSubscription.dueDate().isBefore(LocalDate.now());
    }

    private static boolean isRemovableStatus(final Subscription aSubscription) {
        return aSubscription.isCanceled() || aSubscription.isIncomplete();
    }

    private RuntimeException notFound(Class<? extends AggregateRoot<?>> aggClass, Identifier id) {
        return DomainException.with("%s with id %s was not found".formatted(aggClass.getCanonicalName(), id.value()));
    }

    record StdOutput(SubscriptionId subscriptionId) implements Output {

    }
}

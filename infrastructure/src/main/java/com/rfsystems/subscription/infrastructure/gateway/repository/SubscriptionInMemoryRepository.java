package com.rfsystems.subscription.infrastructure.gateway.repository;

import com.rfsystems.subscription.domain.account.AccountId;
import com.rfsystems.subscription.domain.subscription.Subscription;
import com.rfsystems.subscription.domain.subscription.SubscriptionGateway;
import com.rfsystems.subscription.domain.subscription.SubscriptionId;
import com.rfsystems.subscription.domain.utils.IdUtils;

import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class SubscriptionInMemoryRepository implements SubscriptionGateway {

    private final Map<String, Subscription> db = new ConcurrentHashMap<>();
    private final Map<String, Set<Subscription>> accountIndex = new ConcurrentHashMap<>();

    @Override
    public Optional<Subscription> latestSubscriptionOfAccount(AccountId accountId) {
        return accountIndex.getOrDefault(accountId.value(), Set.of()).stream().findFirst();
    }

    @Override
    public Optional<Subscription> subscriptionOfId(SubscriptionId subscriptionId) {
        return Optional.ofNullable(this.db.get(subscriptionId.value()));
    }

    @Override
    public Subscription save(Subscription subscription) {
        this.db.put(subscription.id().value(), subscription);
        updateAccountIndex(subscription);
        return subscription;
    }

    @Override
    public SubscriptionId nextId() {
        return new SubscriptionId(IdUtils.uniqueId());
    }

    private void updateAccountIndex(Subscription subscription) {
        var bag = this.accountIndex.getOrDefault(subscription.accountId().value(), new HashSet<>());
        bag.add(subscription);
        this.accountIndex.put(subscription.accountId().value(), bag);
    }
}

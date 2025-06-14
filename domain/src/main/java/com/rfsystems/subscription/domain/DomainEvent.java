package com.rfsystems.subscription.domain;

import java.time.Instant;

public interface DomainEvent extends AssertionConcern {
    Instant occurredOn();
    String aggregateId();
    String aggregateType();
}

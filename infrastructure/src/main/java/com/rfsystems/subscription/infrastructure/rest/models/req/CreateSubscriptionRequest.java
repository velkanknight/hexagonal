package com.rfsystems.subscription.infrastructure.rest.models.req;

import jakarta.validation.constraints.NotNull;

public record CreateSubscriptionRequest(@NotNull Long planId) {

}

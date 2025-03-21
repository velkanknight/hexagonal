package com.rfsystems.subscription.infrastructure.rest.models.res;

import com.rfsystems.subscription.application.plan.ChangePlan;
import com.rfsystems.subscription.domain.AssertionConcern;

public record ChangePlanResponse(Long planId) implements AssertionConcern {

    public ChangePlanResponse {
        this.assertArgumentNotNull(planId, "ChangePlanResponse 'planId' should not be null");
    }

    public ChangePlanResponse(ChangePlan.Output out) {
        this(out.planId().value());
    }
}

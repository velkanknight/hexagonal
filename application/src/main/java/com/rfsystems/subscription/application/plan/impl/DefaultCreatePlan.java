package com.rfsystems.subscription.application.plan.impl;

import com.rfsystems.subscription.application.plan.CreatePlan;
import com.rfsystems.subscription.domain.money.Money;
import com.rfsystems.subscription.domain.plan.Plan;
import com.rfsystems.subscription.domain.plan.PlanGateway;
import com.rfsystems.subscription.domain.plan.PlanId;

import java.util.Objects;

public class DefaultCreatePlan extends CreatePlan {

    private final PlanGateway planGateway;

    public DefaultCreatePlan(final PlanGateway planGateway) {
        this.planGateway = Objects.requireNonNull(planGateway);
    }

    @Override
    public CreatePlan.Output execute(final CreatePlan.Input in) {
        var aPlan = newPlanWith(in);
        aPlan = this.planGateway.save(aPlan);
        return new StdOutput(aPlan.id());
    }

    private Plan newPlanWith(final Input in) {
        return Plan.newPlan(
                this.planGateway.nextId(),
                in.name(), in.description(), in.active(),
                new Money(in.currency(), in.price())
        );
    }

    record StdOutput(PlanId planId) implements CreatePlan.Output {}
}

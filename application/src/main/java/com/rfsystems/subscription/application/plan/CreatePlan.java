package com.rfsystems.subscription.application.plan;

import com.rfsystems.subscription.application.UseCase;
import com.rfsystems.subscription.domain.plan.PlanId;

public abstract class CreatePlan extends UseCase<CreatePlan.Input, CreatePlan.Output> {

    public interface Input {
        String name();
        String description();
        Double price();
        String currency();
        Boolean active();
    }

    public interface Output {
        PlanId planId();
    }
}

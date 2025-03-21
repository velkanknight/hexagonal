package com.rfsystems.subscription.application.plan;

import com.rfsystems.subscription.application.UseCase;
import com.rfsystems.subscription.domain.plan.PlanId;

public abstract class ChangePlan extends UseCase<ChangePlan.Input, ChangePlan.Output> {

    public interface Input {
        Long planId();
        String name();
        String description();
        String currency();
        Double price();
        Boolean active();
    }

    public interface Output {
        PlanId planId();
    }
}

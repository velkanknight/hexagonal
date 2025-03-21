package com.rfsystems.subscription.domain.plan;

import com.rfsystems.subscription.domain.money.Money;

public sealed interface PlanCommand {

    record ChangePlan(String name, String description, Money price, Boolean active) implements PlanCommand {

    }

    record InactivatePlan() implements PlanCommand {

    }

    record ActivatePlan() implements PlanCommand {

    }
}

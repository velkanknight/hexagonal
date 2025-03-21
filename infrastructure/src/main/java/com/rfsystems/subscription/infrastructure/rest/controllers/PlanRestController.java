package com.rfsystems.subscription.infrastructure.rest.controllers;

import com.rfsystems.subscription.application.plan.ChangePlan;
import com.rfsystems.subscription.application.plan.CreatePlan;
import com.rfsystems.subscription.domain.exceptions.DomainException;
import com.rfsystems.subscription.infrastructure.rest.PlanRestApi;
import com.rfsystems.subscription.infrastructure.rest.models.req.ChangePlanRequest;
import com.rfsystems.subscription.infrastructure.rest.models.req.CreatePlanRequest;
import com.rfsystems.subscription.infrastructure.rest.models.res.ChangePlanResponse;
import com.rfsystems.subscription.infrastructure.rest.models.res.CreatePlanResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.Objects;

@RestController
public class PlanRestController implements PlanRestApi {

    private final CreatePlan createPlan;
    private final ChangePlan changePlan;

    public PlanRestController(final CreatePlan createPlan, final ChangePlan changePlan) {
        this.createPlan = Objects.requireNonNull(createPlan);
        this.changePlan = Objects.requireNonNull(changePlan);
    }

    @Override
    public ResponseEntity<CreatePlanResponse> createPlan(final CreatePlanRequest req) {
        final var res = this.createPlan.execute(req, CreatePlanResponse::new);
        return ResponseEntity.created(URI.create("/plans/" + res.planId())).body(res);
    }

    @Override
    public ResponseEntity<ChangePlanResponse> changePlan(final Long planId, final ChangePlanRequest req) {
        if (!Objects.equals(req.planId(), planId)) {
            throw DomainException.with("Plan identifier doesn't matches");
        }
        return ResponseEntity.ok().body(this.changePlan.execute(req, ChangePlanResponse::new));
    }
}

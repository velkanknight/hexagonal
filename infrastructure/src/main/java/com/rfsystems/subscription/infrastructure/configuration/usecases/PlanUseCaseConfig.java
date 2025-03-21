package com.rfsystems.subscription.infrastructure.configuration.usecases;

import com.rfsystems.subscription.application.plan.ChangePlan;
import com.rfsystems.subscription.application.plan.CreatePlan;
import com.rfsystems.subscription.application.plan.impl.DefaultChangePlan;
import com.rfsystems.subscription.application.plan.impl.DefaultCreatePlan;
import com.rfsystems.subscription.domain.plan.PlanGateway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
public class PlanUseCaseConfig {

    @Bean
    CreatePlan createPlan(PlanGateway planGateway) {
        return new DefaultCreatePlan(planGateway);
    }

    @Bean
    ChangePlan changePlan(PlanGateway planGateway) {
        return new DefaultChangePlan(planGateway);
    }
}

package com.rfsystems.subscription.infrastructure.configuration.usecases;

import com.rfsystems.subscription.application.subscription.CancelSubscription;
import com.rfsystems.subscription.application.subscription.ChargeSubscription;
import com.rfsystems.subscription.application.subscription.CreateSubscription;
import com.rfsystems.subscription.application.subscription.impl.DefaultCancelSubscription;
import com.rfsystems.subscription.application.subscription.impl.DefaultChargeSubscription;
import com.rfsystems.subscription.application.subscription.impl.DefaultCreateSubscription;
import com.rfsystems.subscription.domain.account.AccountGateway;
import com.rfsystems.subscription.domain.payment.PaymentGateway;
import com.rfsystems.subscription.domain.plan.PlanGateway;
import com.rfsystems.subscription.domain.subscription.SubscriptionGateway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;

@Configuration(proxyBeanMethods = false)
public class SubscriptionUseCaseConfig {

    @Bean
    CreateSubscription createSubscription(
            final AccountGateway accountGateway,
            final PlanGateway planGateway,
            final SubscriptionGateway subscriptionGateway
    ) {
        return new DefaultCreateSubscription(accountGateway, planGateway, subscriptionGateway);
    }

    @Bean
    CancelSubscription cancelSubscription(
            final SubscriptionGateway subscriptionGateway
    ) {
        return new DefaultCancelSubscription(subscriptionGateway);
    }

    @Bean
    ChargeSubscription chargeSubscription(
            final Clock clock,
            final PaymentGateway paymentGateway,
            final PlanGateway planGateway,
            final SubscriptionGateway subscriptionGateway
    ) {
        return new DefaultChargeSubscription(clock, paymentGateway, planGateway, subscriptionGateway);
    }
}

package com.rfsystems.subscription.application.subscription.impl;

import com.rfsystems.subscription.application.subscription.CancelSubscription;
import com.rfsystems.subscription.domain.Fixture;
import com.rfsystems.subscription.domain.UnitTest;
import com.rfsystems.subscription.domain.account.AccountId;
import com.rfsystems.subscription.domain.plan.Plan;
import com.rfsystems.subscription.domain.subscription.Subscription;
import com.rfsystems.subscription.domain.subscription.SubscriptionGateway;
import com.rfsystems.subscription.domain.subscription.SubscriptionId;
import com.rfsystems.subscription.domain.subscription.status.ActiveSubscriptionStatus;
import com.rfsystems.subscription.domain.subscription.status.CanceledSubscriptionStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Optional;

import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

class DefaultCancelSubscriptionTest extends UnitTest {

    @Mock
    private SubscriptionGateway subscriptionGateway;

    @InjectMocks
    private DefaultCancelSubscription target;

    @Test
    public void givenActiveSubscription_whenCallsCancelSubscription_shouldCancelIt() {
        // given
        var expectedPlan = Fixture.Plans.plus();
        var expectedAccount = Fixture.Accounts.john();
        var expectedSubscription = newSubscriptionWith(expectedAccount.id(), expectedPlan, ActiveSubscriptionStatus.ACTIVE, LocalDateTime.now().minusDays(15));
        var expectedSubscriptionId = expectedSubscription.id();
        var expectedSubscriptionStatus = CanceledSubscriptionStatus.CANCELED;

        when(subscriptionGateway.latestSubscriptionOfAccount(eq(expectedAccount.id()))).thenReturn(Optional.of(expectedSubscription));
        when(subscriptionGateway.save(any())).thenAnswer(returnsFirstArg());

        // when
        var actualOutput =
                this.target.execute(new CancelSubscriptionTestInput(expectedSubscriptionId.value(), expectedAccount.id().value()));

        // then
        Assertions.assertEquals(expectedSubscriptionId, actualOutput.subscriptionId());
        Assertions.assertEquals(expectedSubscriptionStatus, actualOutput.subscriptionStatus());
    }

    private static Subscription newSubscriptionWith(AccountId expectedAccountId, Plan plus, String status, LocalDateTime date) {
        final var instant = date.toInstant(ZoneOffset.UTC);
        return Subscription.with(
                new SubscriptionId("SUB123"), 1, expectedAccountId, plus.id(),
                date.toLocalDate(), status,
                instant, "a123",
                instant, instant
        );
    }

    record CancelSubscriptionTestInput(String subscriptionId, String accountId) implements CancelSubscription.Input {

    }
}
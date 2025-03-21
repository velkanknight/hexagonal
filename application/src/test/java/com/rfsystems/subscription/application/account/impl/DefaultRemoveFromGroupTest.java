package com.rfsystems.subscription.application.account.impl;

import com.rfsystems.subscription.application.account.RemoveFromGroup;
import com.rfsystems.subscription.domain.Fixture;
import com.rfsystems.subscription.domain.UnitTest;
import com.rfsystems.subscription.domain.account.AccountGateway;
import com.rfsystems.subscription.domain.account.AccountId;
import com.rfsystems.subscription.domain.account.idp.GroupId;
import com.rfsystems.subscription.domain.account.idp.IdentityProviderGateway;
import com.rfsystems.subscription.domain.plan.Plan;
import com.rfsystems.subscription.domain.subscription.Subscription;
import com.rfsystems.subscription.domain.subscription.SubscriptionGateway;
import com.rfsystems.subscription.domain.subscription.SubscriptionId;
import com.rfsystems.subscription.domain.subscription.status.CanceledSubscriptionStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class DefaultRemoveFromGroupTest extends UnitTest {

    @Mock
    private AccountGateway accountGateway;

    @Mock
    private IdentityProviderGateway identityProviderGateway;

    @Mock
    private SubscriptionGateway subscriptionGateway;

    @InjectMocks
    private DefaultRemoveFromGroup target;

    @Test
    public void givenCanceledSubscriptionAndDueDatePast_whenCallsExecute_shouldCallIdentityProvider() {
        // given
        final var expectedGroupId = new GroupId("GROUP-123");
        final var plus = Fixture.Plans.plus();
        final var john = Fixture.Accounts.john();
        final var expectedAccountId = john.id();
        final var johnsSubscription = newSubscriptionWith(expectedAccountId, plus, CanceledSubscriptionStatus.CANCELED, LocalDateTime.now().minusDays(1));
        final var expectedSubscriptionId = johnsSubscription.id();

        Assertions.assertTrue(johnsSubscription.isCanceled(), "Para esse teste a subscription precisa estar com Canceled status");

        when(subscriptionGateway.subscriptionOfId(any())).thenReturn(Optional.of(johnsSubscription));
        when(accountGateway.accountOfId(any())).thenReturn(Optional.of(john));
        doNothing().when(identityProviderGateway).removeUserFromGroup(any(), any());

        // when
        this.target.execute(new RemoveFromGroupTestInput(expectedAccountId.value(), expectedGroupId.value(), expectedSubscriptionId.value()));

        // then
        verify(subscriptionGateway, times(1)).subscriptionOfId(eq(expectedSubscriptionId));
        verify(accountGateway, times(1)).accountOfId(eq(john.id()));
        verify(identityProviderGateway, times(1)).removeUserFromGroup(eq(john.userId()), eq(expectedGroupId));
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

    record RemoveFromGroupTestInput(
            String accountId,
            String groupId,
            String subscriptionId
    ) implements RemoveFromGroup.Input {

    }
}
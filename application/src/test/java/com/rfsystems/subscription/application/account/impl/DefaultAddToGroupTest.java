package com.rfsystems.subscription.application.account.impl;

import com.rfsystems.subscription.application.account.AddToGroup;
import com.rfsystems.subscription.domain.Fixture;
import com.rfsystems.subscription.domain.UnitTest;
import com.rfsystems.subscription.domain.account.AccountGateway;
import com.rfsystems.subscription.domain.account.idp.GroupId;
import com.rfsystems.subscription.domain.account.idp.IdentityProviderGateway;
import com.rfsystems.subscription.domain.subscription.SubscriptionGateway;
import com.rfsystems.subscription.domain.subscription.SubscriptionId;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class DefaultAddToGroupTest extends UnitTest {

    @Mock
    private AccountGateway accountGateway;

    @Mock
    private IdentityProviderGateway identityProviderGateway;

    @Mock
    private SubscriptionGateway subscriptionGateway;

    @InjectMocks
    private DefaultAddToGroup target;

    @Test
    public void givenTrailSubscription_whenCallsExecute_shouldCallIdentityProvider() {
        // given
        final var john = Fixture.Accounts.john();
        final var expectedGroupId = new GroupId("GROUP-123");
        final var expectedAccountId = john.id();
        final var expectedSubscriptionId = new SubscriptionId("SUB-123");
        final var johnsSubscription = Fixture.Subscriptions.johns();

        Assertions.assertTrue(johnsSubscription.isTrail(), "Para esse teste a subscription precisa estar com Trial");

        when(subscriptionGateway.subscriptionOfId(any())).thenReturn(Optional.of(johnsSubscription));
        when(accountGateway.accountOfId(any())).thenReturn(Optional.of(john));
        doNothing().when(identityProviderGateway).addUserToGroup(any(), any());

        // when
        this.target.execute(new AddToGroupTestInput(expectedAccountId.value(), expectedGroupId.value(), expectedSubscriptionId.value()));

        // then
        verify(subscriptionGateway, times(1)).subscriptionOfId(eq(expectedSubscriptionId));
        verify(accountGateway, times(1)).accountOfId(eq(john.id()));
        verify(identityProviderGateway, times(1)).addUserToGroup(eq(john.userId()), eq(expectedGroupId));
    }

    record AddToGroupTestInput(
            String accountId,
            String groupId,
            String subscriptionId
    ) implements AddToGroup.Input {

    }
}
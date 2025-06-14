package com.rfsystems.subscription.application.subscription.impl;

import com.rfsystems.subscription.application.subscription.ChargeSubscription;
import com.rfsystems.subscription.domain.Fixture;
import com.rfsystems.subscription.domain.UnitTest;
import com.rfsystems.subscription.domain.account.AccountGateway;
import com.rfsystems.subscription.domain.account.AccountId;
import com.rfsystems.subscription.domain.payment.Payment;
import com.rfsystems.subscription.domain.payment.PaymentGateway;
import com.rfsystems.subscription.domain.payment.PixPayment;
import com.rfsystems.subscription.domain.payment.Transaction;
import com.rfsystems.subscription.domain.person.Address;
import com.rfsystems.subscription.domain.plan.Plan;
import com.rfsystems.subscription.domain.plan.PlanGateway;
import com.rfsystems.subscription.domain.subscription.Subscription;
import com.rfsystems.subscription.domain.subscription.SubscriptionGateway;
import com.rfsystems.subscription.domain.subscription.SubscriptionId;
import com.rfsystems.subscription.domain.subscription.status.SubscriptionStatus;
import com.rfsystems.subscription.domain.utils.InstantUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class DefaultChargeSubscriptionTest extends UnitTest {

    @Mock
    private AccountGateway accountGateway;

    @Mock
    private Clock clock;

    @Mock
    private PaymentGateway paymentGateway;

    @Mock
    private PlanGateway planGateway;

    @Mock
    private SubscriptionGateway subscriptionGateway;

    @InjectMocks
    private DefaultChargeSubscription target;

    @Captor
    private ArgumentCaptor<Payment> paymentCaptor;

    @Test
    public void givenSubscriptionOutOfChargePeriod_whenCallsChargeSubscription_shouldSkipCharges() {
        // given
        var referenceDate = LocalDateTime.now().plusDays(2);
        var expectedPlan = Fixture.Plans.plus();
        var expectedAccount = Fixture.Accounts.john();
        var expectedStatus = SubscriptionStatus.ACTIVE;
        var expectedSubscription = newSubscriptionWith(expectedAccount.id(), expectedPlan, expectedStatus, referenceDate);
        var expectedDueDate = referenceDate.toLocalDate();

        when(clock.instant()).thenReturn(InstantUtils.now());
        when(subscriptionGateway.latestSubscriptionOfAccount(eq(expectedAccount.id()))).thenReturn(Optional.of(expectedSubscription));

        // when
        var actualOutput =
                this.target.execute(new ChargeSubscriptionTestInput(expectedAccount.id().value(), Payment.PIX, "", new ChargeSubscriptionAddressTestInput(expectedAccount.billingAddress())));

        // then
        Assertions.assertEquals(expectedSubscription.id(), actualOutput.subscriptionId());
        Assertions.assertEquals(expectedStatus, actualOutput.subscriptionStatus());
        Assertions.assertEquals(expectedDueDate, actualOutput.subscriptionDueDate());
        Assertions.assertNull(actualOutput.paymentTransaction());
    }

    @Test
    public void givenSubscriptionWithPastDueDate_whenChargeSuccessfully_shouldSaveAsActive() {
        // given
        var referenceDate = LocalDateTime.now().minusDays(1);
        var expectedPlan = Fixture.Plans.plus();
        var expectedAccount = Fixture.Accounts.john();
        var expectedStatus = SubscriptionStatus.ACTIVE;
        var expectedSubscription = newSubscriptionWith(expectedAccount.id(), expectedPlan, SubscriptionStatus.INCOMPLETE, referenceDate);
        var expectedDueDate = referenceDate.toLocalDate().plusMonths(1);
        var expectedTransaction = Transaction.success("TRS-123");

        when(clock.instant()).thenReturn(InstantUtils.now());
        when(subscriptionGateway.latestSubscriptionOfAccount(any())).thenReturn(Optional.of(expectedSubscription));
        when(planGateway.planOfId(any())).thenReturn(Optional.of(expectedPlan));
        when(paymentGateway.processPayment(any())).thenReturn(expectedTransaction);

        // when
        var actualOutput =
                this.target.execute(new ChargeSubscriptionTestInput(expectedAccount.id().value(), Payment.PIX, "", new ChargeSubscriptionAddressTestInput(expectedAccount.billingAddress())));

        // then
        Assertions.assertEquals(expectedSubscription.id(), actualOutput.subscriptionId());
        Assertions.assertEquals(expectedStatus, actualOutput.subscriptionStatus());
        Assertions.assertEquals(expectedDueDate, actualOutput.subscriptionDueDate());
        Assertions.assertEquals(expectedTransaction, actualOutput.paymentTransaction());

        verify(subscriptionGateway, times(1)).latestSubscriptionOfAccount(eq(expectedAccount.id()));
        verify(planGateway, times(1)).planOfId(eq(expectedPlan.id()));
        verify(subscriptionGateway, times(1)).save(any());
        verify(paymentGateway, times(1)).processPayment(paymentCaptor.capture());

        var actualPayment = paymentCaptor.getValue();
        Assertions.assertInstanceOf(PixPayment.class, actualPayment);
        Assertions.assertNotNull(actualPayment.orderId());
        Assertions.assertEquals(expectedPlan.price().amount(), actualPayment.amount());
    }

    @Test
    public void givenSubscriptionWithDueDateNow_whenChargeSuccessfully_shouldSaveAsActive() {
        // given
        var referenceDate = LocalDateTime.now();
        var expectedPlan = Fixture.Plans.plus();
        var expectedAccount = Fixture.Accounts.john();
        var expectedStatus = SubscriptionStatus.ACTIVE;
        var expectedSubscription = newSubscriptionWith(expectedAccount.id(), expectedPlan, SubscriptionStatus.ACTIVE, referenceDate);
        var expectedDueDate = referenceDate.toLocalDate().plusMonths(1);
        var expectedTransaction = Transaction.success("TRS-123");

        when(clock.instant()).thenReturn(InstantUtils.now());
        when(subscriptionGateway.latestSubscriptionOfAccount(any())).thenReturn(Optional.of(expectedSubscription));
        when(planGateway.planOfId(any())).thenReturn(Optional.of(expectedPlan));
        when(paymentGateway.processPayment(any())).thenReturn(expectedTransaction);

        // when
        var actualOutput =
                this.target.execute(new ChargeSubscriptionTestInput(expectedAccount.id().value(), Payment.PIX, "", new ChargeSubscriptionAddressTestInput(expectedAccount.billingAddress())));

        // then
        Assertions.assertEquals(expectedSubscription.id(), actualOutput.subscriptionId());
        Assertions.assertEquals(expectedStatus, actualOutput.subscriptionStatus());
        Assertions.assertEquals(expectedDueDate, actualOutput.subscriptionDueDate());
        Assertions.assertEquals(expectedTransaction, actualOutput.paymentTransaction());

        verify(subscriptionGateway, times(1)).latestSubscriptionOfAccount(eq(expectedAccount.id()));
        verify(planGateway, times(1)).planOfId(eq(expectedPlan.id()));
        verify(subscriptionGateway, times(1)).save(any());
        verify(paymentGateway, times(1)).processPayment(paymentCaptor.capture());

        var actualPayment = paymentCaptor.getValue();
        Assertions.assertInstanceOf(PixPayment.class, actualPayment);
        Assertions.assertNotNull(actualPayment.orderId());
        Assertions.assertEquals(expectedPlan.price().amount(), actualPayment.amount());
    }

    @Test
    public void givenSubscriptionWithDueDateNow_whenChargeFailure_shouldSaveAsIncomplete() {
        // given
        var referenceDate = LocalDateTime.now();
        var expectedPlan = Fixture.Plans.plus();
        var expectedAccount = Fixture.Accounts.john();
        var expectedStatus = SubscriptionStatus.INCOMPLETE;
        var expectedSubscription = newSubscriptionWith(expectedAccount.id(), expectedPlan, SubscriptionStatus.ACTIVE, referenceDate);
        var expectedDueDate = referenceDate.toLocalDate();
        var expectedTransaction = Transaction.failure("TRS-123", "No funds");

        when(clock.instant()).thenReturn(InstantUtils.now());
        when(subscriptionGateway.latestSubscriptionOfAccount(any())).thenReturn(Optional.of(expectedSubscription));
        when(planGateway.planOfId(any())).thenReturn(Optional.of(expectedPlan));
        when(paymentGateway.processPayment(any())).thenReturn(expectedTransaction);

        // when
        var actualOutput =
                this.target.execute(new ChargeSubscriptionTestInput(expectedAccount.id().value(), Payment.PIX, "", new ChargeSubscriptionAddressTestInput(expectedAccount.billingAddress())));

        // then
        Assertions.assertEquals(expectedSubscription.id(), actualOutput.subscriptionId());
        Assertions.assertEquals(expectedStatus, actualOutput.subscriptionStatus());
        Assertions.assertEquals(expectedDueDate, actualOutput.subscriptionDueDate());
        Assertions.assertEquals(expectedTransaction, actualOutput.paymentTransaction());

        verify(subscriptionGateway, times(1)).latestSubscriptionOfAccount(eq(expectedAccount.id()));
        verify(planGateway, times(1)).planOfId(eq(expectedPlan.id()));
        verify(subscriptionGateway, times(1)).save(any());
        verify(paymentGateway, times(1)).processPayment(paymentCaptor.capture());

        var actualPayment = paymentCaptor.getValue();
        Assertions.assertInstanceOf(PixPayment.class, actualPayment);
        Assertions.assertNotNull(actualPayment.orderId());
        Assertions.assertEquals(expectedPlan.price().amount(), actualPayment.amount());
        verify(subscriptionGateway, times(1)).save(any());
    }

    @Test
    public void givenSubscriptionWithPastDueDate_whenChargeFailureAndPastMaxIncompleteDays_shouldSaveAsCanceled() {
        // given
        var referenceDate = LocalDateTime.now().minusDays(4);
        var expectedPlan = Fixture.Plans.plus();
        var expectedAccount = Fixture.Accounts.john();
        var expectedStatus = SubscriptionStatus.CANCELED;
        var expectedSubscription = newSubscriptionWith(expectedAccount.id(), expectedPlan, SubscriptionStatus.INCOMPLETE, referenceDate);
        var expectedDueDate = referenceDate.toLocalDate();
        var expectedTransaction = Transaction.failure("TRS-555", "No funds");

        when(clock.instant()).thenReturn(InstantUtils.now());
        when(subscriptionGateway.latestSubscriptionOfAccount(any())).thenReturn(Optional.of(expectedSubscription));
        when(planGateway.planOfId(any())).thenReturn(Optional.of(expectedPlan));
        when(paymentGateway.processPayment(any())).thenReturn(expectedTransaction);

        // when
        var actualOutput =
                this.target.execute(new ChargeSubscriptionTestInput(expectedAccount.id().value(), Payment.PIX, "", new ChargeSubscriptionAddressTestInput(expectedAccount.billingAddress())));

        // then
        Assertions.assertEquals(expectedSubscription.id(), actualOutput.subscriptionId());
        Assertions.assertEquals(expectedStatus, actualOutput.subscriptionStatus());
        Assertions.assertEquals(expectedDueDate, actualOutput.subscriptionDueDate());
        Assertions.assertEquals(expectedTransaction, actualOutput.paymentTransaction());

        verify(subscriptionGateway, times(1)).latestSubscriptionOfAccount(eq(expectedAccount.id()));
        verify(planGateway, times(1)).planOfId(eq(expectedPlan.id()));
        verify(subscriptionGateway, times(1)).save(any());
        verify(paymentGateway, times(1)).processPayment(paymentCaptor.capture());

        var actualPayment = paymentCaptor.getValue();
        Assertions.assertInstanceOf(PixPayment.class, actualPayment);
        Assertions.assertNotNull(actualPayment.orderId());
        Assertions.assertEquals(expectedPlan.price().amount(), actualPayment.amount());
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

    record ChargeSubscriptionTestInput(
            String accountId,
            String paymentType,
            String creditCardToken,
            Address billingAddress
    ) implements ChargeSubscription.Input {}

    record ChargeSubscriptionAddressTestInput(Address address) implements ChargeSubscription.Input.Address {

        @Override
        public String zipcode() {
            return address.zipcode();
        }

        @Override
        public String number() {
            return address.number();
        }

        @Override
        public String country() {
            return address.country();
        }

        @Override
        public String complement() {
            return address.complement();
        }
    }
}
package com.rfsystems.subscription.application.subscription;

import com.rfsystems.subscription.application.UseCase;
import com.rfsystems.subscription.domain.payment.Transaction;
import com.rfsystems.subscription.domain.subscription.SubscriptionId;

import java.time.LocalDate;

public abstract class ChargeSubscription extends UseCase<ChargeSubscription.Input, ChargeSubscription.Output> {

    public interface Input {
        String accountId();
        String paymentType();
        String creditCardToken();
        Address billingAddress();

        interface Address {
            String zipcode();
            String number();
            String country();
            String complement();
        }
    }

    public interface Output {
        SubscriptionId subscriptionId();
        String subscriptionStatus();
        LocalDate subscriptionDueDate();
        Transaction paymentTransaction();
    }
}

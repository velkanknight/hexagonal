package com.rfsystems.subscription.domain.payment;

public interface PaymentGateway {
    Transaction processPayment(Payment payment);
}

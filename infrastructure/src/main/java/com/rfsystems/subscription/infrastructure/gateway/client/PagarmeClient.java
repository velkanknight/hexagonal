package com.rfsystems.subscription.infrastructure.gateway.client;

import com.rfsystems.subscription.domain.payment.Payment;
import com.rfsystems.subscription.domain.payment.PaymentGateway;
import com.rfsystems.subscription.domain.payment.Transaction;
import com.rfsystems.subscription.domain.utils.IdUtils;
import org.springframework.stereotype.Component;

import java.time.LocalTime;

@Component
public class PagarmeClient implements PaymentGateway {

    @Override
    public Transaction processPayment(Payment payment) {
        if (LocalTime.now().getMinute() % 2 == 0) {
            return Transaction.success(IdUtils.uniqueId());
        } else {
            return Transaction.failure(IdUtils.uniqueId(), "Not enough funds");
        }
    }
}

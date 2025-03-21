package com.rfsystems.subscription.infrastructure.rest.models.req;

import com.rfsystems.subscription.application.subscription.ChargeSubscription;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ChargeSubscriptionRequest(
        @NotBlank String paymentType,
        String creditCardToken,
        @NotNull BillingAddress billingAddress
) {

    public record BillingAddress(
            @NotBlank String zipcode,
            @NotBlank String number,
            String complement,
            @NotBlank String country
    ) implements ChargeSubscription.Input.Address {}
}

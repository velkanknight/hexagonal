package com.rfsystems.subscription.domain.person;

import com.rfsystems.subscription.domain.ValueObject;

public record Address(String zipcode, String number, String complement, String country) implements ValueObject {

    public Address {
        this.assertArgumentNotEmpty(zipcode, "'zipcode' should not be empty");
        this.assertArgumentNotEmpty(number, "'number' should not be empty");
        this.assertArgumentNotEmpty(country, "'country' should not be empty");
    }
}

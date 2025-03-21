package com.rfsystems.subscription.application.account.impl;

import com.rfsystems.subscription.application.account.CreateIdpUser;
import com.rfsystems.subscription.domain.account.AccountId;
import com.rfsystems.subscription.domain.account.idp.IdentityProviderGateway;
import com.rfsystems.subscription.domain.account.idp.User;
import com.rfsystems.subscription.domain.account.idp.UserId;
import com.rfsystems.subscription.domain.person.Email;
import com.rfsystems.subscription.domain.person.Name;

import java.util.Objects;

public class DefaultCreateIdpUser extends CreateIdpUser {

    private final IdentityProviderGateway identityProviderGateway;

    public DefaultCreateIdpUser(final IdentityProviderGateway identityProviderGateway) {
        this.identityProviderGateway = Objects.requireNonNull(identityProviderGateway);
    }

    @Override
    public Output execute(final Input in) {
        if (in == null) {
            throw new IllegalArgumentException("Input to DefaultCreateIdpUser cannot be null");
        }

        return new StdOutput(this.identityProviderGateway.create(this.userWith(in)));
    }

    private User userWith(final Input in) {
        return User.newUser(new AccountId(in.accountId()), new Name(in.firstname(), in.lastname()), new Email(in.email()), in.password());
    }

    record StdOutput(UserId idpUserId) implements Output {

    }
}

package com.rfsystems.subscription.application.account;

import com.rfsystems.subscription.application.UseCase;
import com.rfsystems.subscription.domain.account.idp.UserId;

public abstract class CreateIdpUser extends UseCase<CreateIdpUser.Input, CreateIdpUser.Output> {

    public interface Input {
        String accountId();
        String firstname();
        String lastname();
        String email();
        String password();
    }

    public interface Output {
        UserId idpUserId();
    }
}

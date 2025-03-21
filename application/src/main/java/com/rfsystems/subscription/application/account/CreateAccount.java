package com.rfsystems.subscription.application.account;

import com.rfsystems.subscription.application.UseCase;
import com.rfsystems.subscription.domain.account.AccountId;

public abstract class CreateAccount extends UseCase<CreateAccount.Input, CreateAccount.Output> {

    public interface Input {
        String accountId();
        String userId();
        String email();
        String firstname();
        String lastname();
        String documentNumber();
        String documentType();
    }

    public interface Output {
        AccountId accountId();
    }
}

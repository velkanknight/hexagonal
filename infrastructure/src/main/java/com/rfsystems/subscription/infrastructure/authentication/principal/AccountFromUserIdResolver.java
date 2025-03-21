package com.rfsystems.subscription.infrastructure.authentication.principal;

import com.rfsystems.subscription.domain.account.Account;
import com.rfsystems.subscription.domain.account.idp.UserId;

import java.util.Optional;
import java.util.function.Function;

public interface AccountFromUserIdResolver extends Function<UserId, Optional<Account>> {
}

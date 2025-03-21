package com.rfsystems.subscription.domain.account;

import com.rfsystems.subscription.domain.person.Address;
import com.rfsystems.subscription.domain.person.Document;
import com.rfsystems.subscription.domain.person.Email;
import com.rfsystems.subscription.domain.person.Name;

public sealed interface AccountCommand {

    record ChangeProfileCommand(Name aName, Address aBillingAddress) implements AccountCommand {

    }

    record ChangeEmailCommand(Email anEmail) implements AccountCommand {

    }

    record ChangeDocumentCommand(Document aDocument) implements AccountCommand {

    }
}

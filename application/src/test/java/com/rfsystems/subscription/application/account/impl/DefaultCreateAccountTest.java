package com.rfsystems.subscription.application.account.impl;

import com.rfsystems.subscription.application.account.CreateAccount;
import com.rfsystems.subscription.domain.UnitTest;
import com.rfsystems.subscription.domain.account.AccountGateway;
import com.rfsystems.subscription.domain.account.AccountId;
import com.rfsystems.subscription.domain.account.idp.UserId;
import com.rfsystems.subscription.domain.person.Document;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class DefaultCreateAccountTest extends UnitTest {

    @Mock
    private AccountGateway accountGateway;

    @InjectMocks
    private DefaultCreateAccount target;

    @Test
    public void givenValidInput_whenCallsExecute_shouldReturnAccountID() {
        // given
        var expectedFirstname = "John";
        var expectedLastname = "Doe";
        var expectedEmail = "john@gmail.com";
        var expectedDocumentType = Document.Cpf.TYPE;
        var expectedDocumentNumber = "12312312323";
        var expectedUserId = new UserId("123");
        var expectedAccountId = new AccountId("ACC-123");

        when(accountGateway.save(any())).thenAnswer(returnsFirstArg());

        // when
        var actualOutput = this.target.execute(new CreateAccountTestInput(expectedAccountId.value(), expectedUserId.value(), expectedFirstname, expectedLastname, expectedEmail, expectedDocumentType, expectedDocumentNumber));

        // then
        Assertions.assertEquals(expectedAccountId, actualOutput.accountId());
    }

    record CreateAccountTestInput(
            String accountId,
            String userId,
            String firstname,
            String lastname,
            String email,
            String documentType,
            String documentNumber
    ) implements CreateAccount.Input {

    }
}
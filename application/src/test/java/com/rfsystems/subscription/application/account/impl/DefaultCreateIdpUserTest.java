package com.rfsystems.subscription.application.account.impl;

import com.rfsystems.subscription.application.account.CreateIdpUser;
import com.rfsystems.subscription.domain.UnitTest;
import com.rfsystems.subscription.domain.account.idp.IdentityProviderGateway;
import com.rfsystems.subscription.domain.account.idp.UserId;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class rfsystemsDefaultCreateIdpUserTest extends UnitTest {

    @Mock
    private IdentityProviderGateway identityProviderGateway;

    @InjectMocks
    private DefaultCreateIdpUser target;

    @Test
    public void givenValidInput_whenCallsExecute_shouldReturnUserID() {
        // given
        var expectedAccountId = "ACC123";
        var expectedFirstname = "John";
        var expectedLastname = "Doe";
        var expectedEmail = "john@gmail.com";
        var expectedPassword = "123456";
        var expectedUserId = new UserId("123");

        when(identityProviderGateway.create(any())).thenReturn(expectedUserId);

        // when
        var actualOutput = this.target.execute(new CreateIdpUserTestInput(expectedAccountId, expectedFirstname, expectedLastname, expectedEmail, expectedPassword));

        // then
        Assertions.assertEquals(expectedUserId, actualOutput.idpUserId());
    }

    record CreateIdpUserTestInput(String accountId, String firstname, String lastname, String email, String password) implements CreateIdpUser.Input {

    }
}
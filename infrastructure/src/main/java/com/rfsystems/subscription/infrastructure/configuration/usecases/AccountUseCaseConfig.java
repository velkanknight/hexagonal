package com.rfsystems.subscription.infrastructure.configuration.usecases;

import com.rfsystems.subscription.application.account.AddToGroup;
import com.rfsystems.subscription.application.account.CreateAccount;
import com.rfsystems.subscription.application.account.CreateIdpUser;
import com.rfsystems.subscription.application.account.RemoveFromGroup;
import com.rfsystems.subscription.application.account.impl.DefaultAddToGroup;
import com.rfsystems.subscription.application.account.impl.DefaultCreateAccount;
import com.rfsystems.subscription.application.account.impl.DefaultCreateIdpUser;
import com.rfsystems.subscription.application.account.impl.DefaultRemoveFromGroup;
import com.rfsystems.subscription.domain.account.AccountGateway;
import com.rfsystems.subscription.domain.account.idp.IdentityProviderGateway;
import com.rfsystems.subscription.domain.subscription.SubscriptionGateway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
public class AccountUseCaseConfig {

    @Bean
    CreateAccount createAccount(final AccountGateway accountGateway) {
        return new DefaultCreateAccount(accountGateway);
    }

    @Bean
    CreateIdpUser createIdpUser(final IdentityProviderGateway identityProviderGateway) {
        return new DefaultCreateIdpUser(identityProviderGateway);
    }

    @Bean
    AddToGroup addToGroup(
            final AccountGateway accountGateway,
            final IdentityProviderGateway identityProviderGateway,
            final SubscriptionGateway subscriptionGateway
    ) {
        return new DefaultAddToGroup(accountGateway, identityProviderGateway, subscriptionGateway);
    }

    @Bean
    RemoveFromGroup removeFromGroup(
            final AccountGateway accountGateway,
            final IdentityProviderGateway identityProviderGateway,
            final SubscriptionGateway subscriptionGateway
    ) {
        return new DefaultRemoveFromGroup(accountGateway, identityProviderGateway, subscriptionGateway);
    }
}

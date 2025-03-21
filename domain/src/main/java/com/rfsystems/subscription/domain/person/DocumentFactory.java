package com.rfsystems.subscription.domain.person;

import com.rfsystems.subscription.domain.payment.exceptions.DomainException;

public final class DocumentFactory {

    private DocumentFactory() {}

    public static Document create(final String documentNumber, final String documentType) {
        return switch (documentType) {
            case Document.Cpf.TYPE -> new Document.Cpf(documentNumber);
            case Document.Cnpj.TYPE -> new Document.Cnpj(documentNumber);
            default -> throw DomainException.with("Invalid document type");
        };
    }
}

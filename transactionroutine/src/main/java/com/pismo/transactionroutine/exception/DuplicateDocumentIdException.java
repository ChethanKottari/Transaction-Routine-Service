package com.pismo.transactionroutine.exception;

public class DuplicateDocumentIdException extends RuntimeException {
    public DuplicateDocumentIdException(String documentNumber) {
        super("Account already exists with document number: " + documentNumber);
    }
}
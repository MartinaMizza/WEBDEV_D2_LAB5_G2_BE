package it.packovery.service.exception;

public class AccountPermanentlyBlockedException extends RuntimeException {

    public AccountPermanentlyBlockedException(String message) {
        super(message);
    }
}

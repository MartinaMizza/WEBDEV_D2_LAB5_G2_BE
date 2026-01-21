package it.packovery.service.exception;

public class AccountTemporarilyBlockedException extends RuntimeException {

    public AccountTemporarilyBlockedException(String message) {
        super(message);
    }
}

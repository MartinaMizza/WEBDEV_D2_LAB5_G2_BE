package it.packovery.service.exception;

public class UnmatchingPasswordsException extends RuntimeException {
    public UnmatchingPasswordsException(String message) {
        super(message);
    }
}

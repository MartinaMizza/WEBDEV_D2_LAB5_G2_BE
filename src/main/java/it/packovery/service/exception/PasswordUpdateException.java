package it.packovery.service.exception;

public class PasswordUpdateException extends RuntimeException {

    public PasswordUpdateException(String message, Throwable cause) {
        super(message, cause);
    }
}

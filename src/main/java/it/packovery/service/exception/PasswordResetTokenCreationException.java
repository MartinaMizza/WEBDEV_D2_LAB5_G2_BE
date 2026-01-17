package it.packovery.service.exception;

public class PasswordResetTokenCreationException extends RuntimeException {

    public PasswordResetTokenCreationException(String message, Throwable cause) {
        super(message, cause);
    }
}

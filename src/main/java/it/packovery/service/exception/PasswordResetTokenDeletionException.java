package it.packovery.service.exception;

public class PasswordResetTokenDeletionException extends RuntimeException {

    public PasswordResetTokenDeletionException(String message, Throwable cause) {
        super(message, cause);
    }
}

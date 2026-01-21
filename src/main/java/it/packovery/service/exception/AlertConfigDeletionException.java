package it.packovery.service.exception;

public class AlertConfigDeletionException extends RuntimeException {
    public AlertConfigDeletionException(String message, Throwable cause) {
        super(message, cause);
    }
}

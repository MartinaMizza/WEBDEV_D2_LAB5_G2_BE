package it.packovery.service.exception;

public class AlertConfigAlreadyExistsException extends RuntimeException {
    public AlertConfigAlreadyExistsException(String message) {
        super(message);
    }
}

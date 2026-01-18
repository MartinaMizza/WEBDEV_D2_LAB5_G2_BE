package it.packovery.service.exception;

public class OtpSendingException extends RuntimeException {

    public OtpSendingException(String message, Throwable cause) {
        super(message, cause);
    }
}

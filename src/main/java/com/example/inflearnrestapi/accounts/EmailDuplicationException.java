package com.example.inflearnrestapi.accounts;

public class EmailDuplicationException extends RuntimeException {
    public EmailDuplicationException(String message) {
        super(message);
    }

    public EmailDuplicationException(String message, Throwable cause) {
        super(message, cause);
    }
}

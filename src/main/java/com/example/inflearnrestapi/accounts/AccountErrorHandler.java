package com.example.inflearnrestapi.accounts;

import com.example.inflearnrestapi.common.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class AccountErrorHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    public ErrorResponse handleEmailDuplicationException(EmailDuplicationException ex) {
        return new ErrorResponse(ex.getMessage());
    }
}

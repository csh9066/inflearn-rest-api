package com.example.inflearnrestapi.common;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class ExceptionController {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    public ErrorsModel handleBindException(MethodArgumentNotValidException ex) {
        List<ErrorDetail> details = ex.getFieldErrors()
                .stream()
                .map(error -> new ErrorDetail(error.getObjectName(),
                        error.getField(),
                        error.getDefaultMessage()
                ))
                .collect(Collectors.toList());

        ErrorResponse response = new ErrorResponse("유효하지 않은 필드 입니다.", details);
        return new ErrorsModel(response);
    }
}

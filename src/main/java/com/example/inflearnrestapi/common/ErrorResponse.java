package com.example.inflearnrestapi.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Getter
public class ErrorResponse {

    private String message;

    private List<ErrorDetail> errors;

    public ErrorResponse(String message) {
        this.message = message;
    }

    public ErrorResponse(String message, List<ErrorDetail> errors) {
        this.message = message;
        this.errors = errors;
    }
}

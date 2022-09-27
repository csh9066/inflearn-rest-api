package com.example.inflearnrestapi.common;

import lombok.Getter;

@Getter
public class ErrorDetail {
    private String sourceName;

    private String fieldName;

    private String description;

    public ErrorDetail(String sourceName, String fieldName, String description) {
        this.sourceName = sourceName;
        this.fieldName = fieldName;
        this.description = description;
    }
}

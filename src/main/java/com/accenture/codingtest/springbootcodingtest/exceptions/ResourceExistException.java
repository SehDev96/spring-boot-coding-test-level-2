package com.accenture.codingtest.springbootcodingtest.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT)
@Getter
public class ResourceExistException extends RuntimeException {
    private final String fieldName;
    private final String fieldValue;

    public ResourceExistException(String fieldName, String fieldValue) {
        super(String.format("%s :'%s' already exists!", fieldName, fieldValue));
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
    }
}

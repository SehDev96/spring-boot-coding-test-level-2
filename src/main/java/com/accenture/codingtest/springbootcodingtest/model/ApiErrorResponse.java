package com.accenture.codingtest.springbootcodingtest.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiErrorResponse {

    private boolean success = false;
    private int response_code;
    private String error;
    private String message;
    private Date timestamp = new Date();

    public ApiErrorResponse(int response_code, String error, String message) {
        this.response_code = response_code;
        this.error = error;
        this.message = message;
    }
}

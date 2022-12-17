package com.accenture.codingtest.springbootcodingtest.model;

import lombok.Data;
import lombok.NoArgsConstructor;


import java.util.Date;

@Data
@NoArgsConstructor
public class ApiResponse {

    private boolean success = true;
    private int response_code;
    private String message;
    private Date timestamp = new Date();
    private Object payload;

    public ApiResponse(int response_code, String message, Object payload) {
        this.response_code = response_code;
        this.message = message;
        this.payload = payload;
    }
}

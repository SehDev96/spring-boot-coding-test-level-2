package com.accenture.codingtest.springbootcodingtest.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class ApiErrorResponse extends BaseResponseModel{

    private String error;

    public ApiErrorResponse(int response_code, String error, String message) {
        super.setSuccess(false);
        super.setResponse_code(response_code);
        super.setMessage(message);
        this.error = error;
    }
}

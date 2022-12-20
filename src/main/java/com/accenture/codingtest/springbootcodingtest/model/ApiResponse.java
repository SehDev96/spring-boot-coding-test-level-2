package com.accenture.codingtest.springbootcodingtest.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class ApiResponse extends BaseResponseModel{

    private Object payload;

    public ApiResponse(int response_code, String message, Object payload) {
        super.setSuccess(true);
        super.setResponse_code(response_code);
        super.setMessage(message);
        this.payload = payload;
    }
}

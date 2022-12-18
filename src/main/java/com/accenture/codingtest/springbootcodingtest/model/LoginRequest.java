package com.accenture.codingtest.springbootcodingtest.model;

import lombok.Data;

@Data
public class LoginRequest {
    private String username;
    private String password;
}

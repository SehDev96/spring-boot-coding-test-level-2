package com.accenture.codingtest.springbootcodingtest.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Map;

@EqualsAndHashCode(callSuper = true)
@Data
public class ApiAuthResponse extends BaseResponseModel{

    private Map<String,String> payload;
}

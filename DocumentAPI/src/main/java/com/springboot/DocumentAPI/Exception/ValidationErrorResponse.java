package com.springboot.DocumentAPI.Exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
public class ValidationErrorResponse extends SubErrorResponse{
    private String object;
    private String field;
    private Object rejectedValue;
    private String message;

    ValidationErrorResponse(String object, String message) {
        this.object = object;
        this.message = message;
    }
}

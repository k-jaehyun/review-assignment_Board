package com.sparta.plusweekreviewassignment.exception.fieldError;

import lombok.Getter;

import java.util.List;

@Getter
public class FieldErrorException extends RuntimeException{
    private final Integer status;
    private final List<FieldErrorDto> fieldErrorList;

    public FieldErrorException(String message,Integer status, List<FieldErrorDto> fieldErrorList) {
        super(message);
        this.status=status;
        this.fieldErrorList = fieldErrorList;
    }

}

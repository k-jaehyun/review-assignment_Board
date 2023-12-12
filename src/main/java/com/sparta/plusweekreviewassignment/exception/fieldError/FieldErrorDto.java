package com.sparta.plusweekreviewassignment.exception.fieldError;

import lombok.Getter;
import org.springframework.validation.FieldError;

@Getter
public class FieldErrorDto {
    private String errorField;
    private String correntPoint;

    public FieldErrorDto(FieldError fieldError) {
        this.errorField= fieldError.getField();
        this.correntPoint= fieldError.getDefaultMessage();
    }

}

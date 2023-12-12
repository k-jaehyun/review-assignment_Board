package com.sparta.plusweekreviewassignment.exception.fieldError;

import com.sparta.plusweekreviewassignment.CommonResponseDto;
import lombok.Getter;

import java.util.List;

@Getter
public class FieldErrorExceptionDto extends CommonResponseDto {
    private List<FieldErrorDto> fieldErrorList;


    public FieldErrorExceptionDto(String message, Integer status, List<FieldErrorDto> fieldErrorDtoList) {
        super(message,status);
        this.fieldErrorList =fieldErrorDtoList;
    }
}

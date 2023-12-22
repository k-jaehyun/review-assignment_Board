package com.sparta.plusweekreviewassignment.exception;

import com.sparta.plusweekreviewassignment.common.dto.CommonResponseDto;
import com.sparta.plusweekreviewassignment.exception.fieldError.FieldErrorException;
import com.sparta.plusweekreviewassignment.exception.fieldError.FieldErrorExceptionDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({IllegalArgumentException.class})
    public ResponseEntity<CommonResponseDto> IllegalArgumentExceptionHandler(IllegalArgumentException ex) {
        CommonResponseDto commonResponseDto = new CommonResponseDto(ex.getMessage(), HttpStatus.BAD_REQUEST.value());
        return new ResponseEntity<>(commonResponseDto,HttpStatus.BAD_REQUEST);
    }

    // 출력창에서 원하는 메세지만 출력하기 위해 FieldErrorException을 그대로 response에 넣지 않고 DTO를 생성했습니다
    @ExceptionHandler({FieldErrorException.class})
    public ResponseEntity<FieldErrorExceptionDto> FieldErrorExceptionHandler(FieldErrorException ex) {
        FieldErrorExceptionDto fieldErrorExceptionDto = new FieldErrorExceptionDto(ex.getMessage(),ex.getStatus(),ex.getFieldErrorList());
        return new ResponseEntity<>(fieldErrorExceptionDto, HttpStatus.BAD_REQUEST);
    }
}

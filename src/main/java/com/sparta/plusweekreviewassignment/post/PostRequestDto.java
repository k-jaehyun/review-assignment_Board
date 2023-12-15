package com.sparta.plusweekreviewassignment.post;

import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class PostRequestDto {

    @Size(max = 500, message = "제목 500자 이하")
    private String title;
    @Size(max = 5000, message = "작성 내용 5000자 이하")
    private String content;
}

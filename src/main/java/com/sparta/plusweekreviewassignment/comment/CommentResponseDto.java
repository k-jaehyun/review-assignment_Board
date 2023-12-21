package com.sparta.plusweekreviewassignment.comment;

import lombok.Getter;

@Getter
public class CommentResponseDto {

    private String content;

    public CommentResponseDto(Comment comment) {
        this.content=comment.getContent();
    }
}

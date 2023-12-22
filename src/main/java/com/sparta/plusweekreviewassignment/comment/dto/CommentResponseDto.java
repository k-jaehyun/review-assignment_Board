package com.sparta.plusweekreviewassignment.comment.dto;

import com.sparta.plusweekreviewassignment.comment.Comment;
import lombok.Getter;

@Getter
public class CommentResponseDto {

    private String content;

    public CommentResponseDto(Comment comment) {
        this.content=comment.getContent();
    }
}

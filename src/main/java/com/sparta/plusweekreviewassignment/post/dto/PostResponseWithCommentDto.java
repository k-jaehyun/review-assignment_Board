package com.sparta.plusweekreviewassignment.post.dto;

import com.sparta.plusweekreviewassignment.comment.CommentResponseDto;
import com.sparta.plusweekreviewassignment.post.Post;
import lombok.Getter;

import java.util.List;

@Getter
public class PostResponseWithCommentDto extends PostResponseDto{

    private List<CommentResponseDto> commentResponseDtoList;


    public PostResponseWithCommentDto(Post post) {
        super(post);
        this.commentResponseDtoList=post.getComments().stream().map(CommentResponseDto::new).toList();
    }

    public PostResponseWithCommentDto(Post post, byte[] image) {
        super(post, image);
        this.commentResponseDtoList=post.getComments().stream().map(CommentResponseDto::new).toList();
    }
}

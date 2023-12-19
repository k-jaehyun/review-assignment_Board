package com.sparta.plusweekreviewassignment.post.dto;

import com.sparta.plusweekreviewassignment.post.Post;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class PostResponseDto {

    private String title;
    private String nickname;
    private LocalDateTime createdAt;
    private String content;
    private byte[] image;

    public PostResponseDto(Post post) {
        this.title=post.getTitle();
        this.nickname=post.getUser().getNickname();
        this.createdAt=post.getCreatedAt();
        this.content=post.getContent();
    }

    public PostResponseDto(Post post, byte[] image) {
        this.title=post.getTitle();
        this.nickname=post.getUser().getNickname();
        this.createdAt=post.getCreatedAt();
        this.content=post.getContent();
        this.image=image;
    }
}

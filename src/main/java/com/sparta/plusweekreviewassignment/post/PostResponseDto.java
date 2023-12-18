package com.sparta.plusweekreviewassignment.post;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class PostResponseDto {

    private String title;
    private String nickname;
    private LocalDateTime createdAt;
    private String content;

    public PostResponseDto(Post post) {
        this.title=post.getTitle();
        this.nickname=post.getUser().getNickname();
        this.createdAt=post.getCreatedAt();
        this.content=post.getContent();
    }
}

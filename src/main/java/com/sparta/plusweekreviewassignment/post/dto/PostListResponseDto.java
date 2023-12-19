package com.sparta.plusweekreviewassignment.post.dto;

import com.sparta.plusweekreviewassignment.post.Post;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class PostListResponseDto {

    private String title;
    private String nickname;
    private LocalDateTime createdAt;

    public PostListResponseDto(Post post) {
        this.title = post.getTitle();
        this.nickname = post.getUser().getNickname();
        this.createdAt = post.getCreatedAt();
    }
}

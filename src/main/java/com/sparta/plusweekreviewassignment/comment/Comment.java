package com.sparta.plusweekreviewassignment.comment;

import com.sparta.plusweekreviewassignment.user.entity.User;
import com.sparta.plusweekreviewassignment.comment.dto.CommentRequestDto;
import com.sparta.plusweekreviewassignment.common.Timestamped;
import com.sparta.plusweekreviewassignment.post.Post;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Comment extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String content;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Comment(CommentRequestDto requestDto, Post post, User user) {
        this.content= requestDto.getContent();
        this.post=post;
        this.user=user;
    }

    public void modify(CommentRequestDto requestDto) {
        this.content= requestDto.getContent();
    }
}

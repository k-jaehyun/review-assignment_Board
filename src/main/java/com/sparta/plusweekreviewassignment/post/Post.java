package com.sparta.plusweekreviewassignment.post;

import com.sparta.plusweekreviewassignment.User.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity(name = "post")
@Getter
@NoArgsConstructor
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 500)
    private String title;

    @Column(length = 5000)
    private String content;

    @ManyToOne
    @JoinColumn (name = "user_id")
    private User user;

    @Column
    private LocalDateTime createdAt;

    public Post(PostRequestDto postRequestDto, User user) {
        this.title = postRequestDto.getTitle();
        this.content = postRequestDto.getContent();
        this.user = user;
        this.createdAt = LocalDateTime.now();
    }

}

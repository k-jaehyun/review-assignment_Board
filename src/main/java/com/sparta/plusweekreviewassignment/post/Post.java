package com.sparta.plusweekreviewassignment.post;

import com.sparta.plusweekreviewassignment.user.entity.User;
import com.sparta.plusweekreviewassignment.comment.Comment;
import com.sparta.plusweekreviewassignment.common.Timestamped;
import com.sparta.plusweekreviewassignment.likes.Likes;
import com.sparta.plusweekreviewassignment.post.dto.PostRequestDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity(name = "post")
@Getter
@NoArgsConstructor
public class Post extends Timestamped {

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

    @Lob
    @Column(name = "image", columnDefinition = "LONGBLOB")
    private byte[] image;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Comment> comments = new HashSet<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Likes> likes = new HashSet<>();

    public Post(PostRequestDto postRequestDto, User user) {
        this.title = postRequestDto.getTitle();
        this.content = postRequestDto.getContent();
        this.user = user;
    }

    public Post(String title, String content, byte[] imageByte, User user) {
        this.title = title;
        this.content = content;
        this.user = user;
        this.image=imageByte;
    }

    public Post modify(PostRequestDto requestDto) {
        this.title=requestDto.getTitle();
        this.content=requestDto.getContent();
        return this;
    }

    public void modifyWithImage(byte[] imageByte, String title, String content) {
        this.title=title;
        this.content=content;
        this.image=imageByte;
    }
}

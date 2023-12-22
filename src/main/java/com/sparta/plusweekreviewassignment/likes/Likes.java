package com.sparta.plusweekreviewassignment.likes;


import com.sparta.plusweekreviewassignment.user.entity.User;
import com.sparta.plusweekreviewassignment.post.Post;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Likes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private Boolean islike;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Likes(User user, Post post) {
        this.post=post;
        this.user=user;
        this.islike=true;
    }

    public void likeOrNot(Boolean trueOrFalse) {
        this.islike=trueOrFalse;
    }
}

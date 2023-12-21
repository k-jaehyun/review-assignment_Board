package com.sparta.plusweekreviewassignment.likes;

import com.sparta.plusweekreviewassignment.User.User;
import com.sparta.plusweekreviewassignment.post.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikesRepository extends JpaRepository<Likes,Long> {
    Optional<Likes> findByPostAndUser(Post post, User user);
}

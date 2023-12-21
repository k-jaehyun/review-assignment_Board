package com.sparta.plusweekreviewassignment.comment;

import com.sparta.plusweekreviewassignment.post.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByPost(Post post);
}

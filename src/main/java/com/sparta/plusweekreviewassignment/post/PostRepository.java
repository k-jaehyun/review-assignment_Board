package com.sparta.plusweekreviewassignment.post;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findAllByOrderByCreatedAtDesc();
    void deleteByModifiedAtBefore(LocalDateTime before90Days);
}

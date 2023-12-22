package com.sparta.plusweekreviewassignment.User.emailAuth;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public interface EmailAuthRepository extends JpaRepository<EmailAuth,Long> {
    Optional<EmailAuth> findTopByEmailOrderByCreatedAtDesc(String email);

    void deleteByCreatedAtBefore(LocalDateTime fiveMinAgo);
}

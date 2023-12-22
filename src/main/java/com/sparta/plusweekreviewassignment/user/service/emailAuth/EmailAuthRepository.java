package com.sparta.plusweekreviewassignment.user.service.emailAuth;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public interface EmailAuthRepository extends JpaRepository<EmailAuth,Long> {
    void deleteByCreatedAtBefore(LocalDateTime fiveMinAgo);

    Optional<EmailAuth> findTopByNicknameOrderByCreatedAtDesc(String nickname);
}

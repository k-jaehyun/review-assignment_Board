package com.sparta.plusweekreviewassignment.User.emailAuth;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmailAuthRepository extends JpaRepository<EmailAuth,Long> {
    Optional<EmailAuth> findByEmail(String email);
}

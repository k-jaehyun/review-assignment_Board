package com.sparta.plusweekreviewassignment.user.service.emailAuth;

import com.sparta.plusweekreviewassignment.user.entity.UserBase;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class EmailAuth extends UserBase {

    private String sentCode;

    private LocalDateTime createdAt;

    public EmailAuth(String sentCode, String newNickname, String password, String email) {
        super(newNickname,password,email);
        this.sentCode=sentCode;
        this.createdAt=LocalDateTime.now();
    }
}

package com.sparta.plusweekreviewassignment.User.emailAuth;

import com.sparta.plusweekreviewassignment.User.UserBase;
import com.sparta.plusweekreviewassignment.common.Timestamped;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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

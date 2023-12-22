package com.sparta.plusweekreviewassignment.User.emailAuth;

import com.sparta.plusweekreviewassignment.common.Timestamped;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class EmailAuth extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String sentCode;

    private String nickname;

    private String password;

    private String email;


    public EmailAuth(String sentCode, String newNickname, String password, String email) {
        this.sentCode=sentCode;
        this.nickname=newNickname;
        this.password=password;
        this.email=email;
    }
}

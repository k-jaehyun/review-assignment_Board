package com.sparta.plusweekreviewassignment.User;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class User extends UserBase {

    public User(String nickname, String password, String email) {
        super(nickname, password, email);
    }
}

package com.final_project_leesanghun_team2.fixture;


import com.final_project_leesanghun_team2.domain.UserRole;
import com.final_project_leesanghun_team2.domain.entity.User;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;

public class UserEntityFixture {

    public static User get(String userName, String password) {
        User user = new User();
        user.setId(1);
        user.setUserName(userName);
        user.setPassword(password);
        user.setRegisteredAt(null);
        user.setUpdatedAt(null);
        user.setRegisteredAt(null);
        user.setRole(UserRole.USER);
        return user;
    }

    public static User getUser(String userName, String password) {
        User user = new User();
        user.setId(1);
        user.setUserName(userName);
        user.setPassword(password);
        user.setRole(UserRole.USER);
        user.setRegisteredAt(null);
        return user;
    }
}

package com.final_project_leesanghun_team2.domain.entity;

import com.final_project_leesanghun_team2.domain.UserRole;
import java.util.ArrayList;
import java.util.List;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class User extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;
    private String userName;
    private String password;

    @Enumerated(EnumType.STRING)
    private UserRole role = UserRole.USER;

    @OneToMany(mappedBy = "followUser", cascade = CascadeType.REMOVE)
    private List<Follow> followList = new ArrayList<>();

    @OneToMany(mappedBy = "followingUser", cascade = CascadeType.REMOVE)
    private List<Follow> followingList = new ArrayList<>();

    // 회원가입할때 userName, password -> userName, encodedPw 로 포장
    public static User of(String userName, String encodedPw) {
        // Q: 파라미터를 객체로 선언하는게 좋을까? 기본참조형으로 하는게 좋을까?
        // A: 기본참조형으로 가자. 아이디도 비밀번호처럼 커스텀 될 수 있으니까 유지보수 좋게하기 위해 후자로 ㄱㄱ
        return new User(
                userName,
                encodedPw
        );
    }

    // 위에꺼 쓰려고 만들어 놓은것
    public User(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }
}

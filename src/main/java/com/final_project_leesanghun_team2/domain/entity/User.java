package com.final_project_leesanghun_team2.domain.entity;

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
    @Column(nullable = false, unique = true, length = 50)
    private String username; // 이메일
    @Column(nullable = false, length = 50)
    private String password;

    @Column(nullable = false, unique = true, length = 50)
    private String nickName;

    @Enumerated(EnumType.STRING)
    private UserRole role;

    @Column(nullable = false)
    private Long followNum;
    @Column(nullable = false)
    private Long followingNum;

    // 생성 메서드
    public static User createUser(String username, String encodedPW) {
        return new User(username, encodedPW);
    }
    public User(String userName, String password) {
        this.username = userName;
        this.password = password;
        this.role = UserRole.USER;
        this.followNum = 0L;
        this.followingNum = 0L;
    }

    // 수정 메서드
    public void update(String username) {
        this.username = username;
    }

    // 팔로우 감소 메서드
    public void decreaseFollowNum() {
        this.followingNum--;
        // 상대방 followNum 을 컨트롤 해줘야해
    }

    // 팔로우 증가 메서드
    public void increaseFollowNum() {
        this.followingNum++;
        // 상대방 followNum 을 컨트롤 해줘야해
    }
}

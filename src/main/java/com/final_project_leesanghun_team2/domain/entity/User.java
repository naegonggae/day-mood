package com.final_project_leesanghun_team2.domain.entity;

import com.final_project_leesanghun_team2.domain.dto.user.request.UserJoinRequest;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users")
public class User extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;
    @Column(nullable = false, unique = true, length = 320)
    private String username; // 이메일
    @Column(nullable = false)
    private String password;
    @Column(nullable = false, unique = true, length = 15)
    private String nickName;
    @Enumerated(EnumType.STRING)
    private UserRole role;

    // 생성 메서드
    public static User createUser(UserJoinRequest request, String encodedPW) {
        return new User(request, encodedPW);
    }
    public User(UserJoinRequest request, String password) {
        this.username = request.getUsername();
        this.password = password;
        this.nickName = request.getNickName();
        this.role = UserRole.USER;
    }

    // 수정 메서드
    public void update(String nickName) {
        this.nickName = nickName;
    }

}
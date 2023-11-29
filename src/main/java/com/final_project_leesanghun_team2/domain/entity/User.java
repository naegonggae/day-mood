package com.final_project_leesanghun_team2.domain.entity;

import com.final_project_leesanghun_team2.domain.dto.user.UserJoinRequest;
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
    @Column(nullable = false)
    private String password;
    @Column(nullable = false, unique = true, length = 50)
    private String nickName;
    @Enumerated(EnumType.STRING)
    private UserRole role;
    @Column(nullable = false)
    private Long followNum;
    @Column(nullable = false)
    private Long followingNum;
    // 상태 메세지 정보

    // 생성 메서드
    public static User createUser(UserJoinRequest request, String encodedPW) {
        return new User(request, encodedPW);
    }
    public User(UserJoinRequest request, String password) {
        this.username = request.getUsername();
        this.password = password;
        this.nickName = request.getNickName();
        this.role = UserRole.USER;
        this.followNum = 0L;
        this.followingNum = 0L;
    }

    // 수정 메서드
    public void update(String nickName) {
        this.nickName = nickName;
    }

    // 팔로우 감소 메서드
    public void decreaseFollowingNum() {
        this.followingNum--;
        // 상대방 followNum 을 컨트롤 해줘야해
    }
    public void decreaseFollowNum() {
        this.followNum--;
        // 상대방 followNum 을 컨트롤 해줘야해
    }

    // 팔로우 증가 메서드
    public void increaseFollowingNum() {
        this.followingNum++;
        // 상대방 followNum 을 컨트롤 해줘야해
    }
    public void increaseFollowNum() {
        this.followNum++;
        // 상대방 followNum 을 컨트롤 해줘야해
    }
}

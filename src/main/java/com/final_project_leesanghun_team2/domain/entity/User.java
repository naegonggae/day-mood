package com.final_project_leesanghun_team2.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    private String username;
    private String password;

    @Enumerated(EnumType.STRING)
    private UserRole role;

    private Long followNum;
    private Long followingNum;

    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    private List<Post> postList = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "followUser", cascade = CascadeType.REMOVE)
    private List<Follow> followList = new ArrayList<>(); // 내가 팔로우 하는 사람들

    @JsonIgnore
    @OneToMany(mappedBy = "followingUser", cascade = CascadeType.REMOVE)
    private List<Follow> followingList = new ArrayList<>(); // 나를 팔로우잉하는 사람들

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

    // 수정
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

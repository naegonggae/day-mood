package com.final_project_leesanghun_team2.domain.entity;

import java.time.LocalDate;
import java.util.ArrayList;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Post extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long id;

    @NotNull
    @Size(min=2, max=50, message = "내용을 2글자 이상, 100글자 이하로 입력해주세요.")
    private String content;

    private Boolean visibility = true;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "post", cascade = CascadeType.REMOVE)
    private List<Comment> commentList = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.REMOVE) // cascade 여부 고려
    private List<Likes> likesList = new ArrayList<>();

    // 포스트 등록할때 필요정보 DB에 저장
    public static Post of(String title, String body,  User user) {
        return new Post(
                title,
                body,
                user
        );
    }

    public Post(String title, String body, User user, LocalDate birthday) {
        this.title = title;
        this.body = body;
        this.user = user;
        this.birthday = birthday;
    }

    // 위에꺼 쓰려고 만듬
    public Post(String title, String body, User user) {
        this.title = title;
        this.body = body;
        this.user = user;
    }

}

package com.final_project_leesanghun_team2.domain.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "comment", indexes = {
        @Index(name = "post_id_idx", columnList = "post_id")
})
public class Comment extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long id;

    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    // 댓글 등록할때 저장정보 포장
    public static Comment of(String comment, User user, Post post) {
        return new Comment(
                comment,
                user,
                post
        );
    }

    // 위에꺼 하려고 생성
    public Comment(String comment, User user, Post post) {
        this.comment = comment;
        this.user = user;
        this.post = post;
    }
}

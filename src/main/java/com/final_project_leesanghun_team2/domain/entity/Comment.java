package com.final_project_leesanghun_team2.domain.entity;

import com.final_project_leesanghun_team2.domain.dto.comment.request.CommentSaveRequest;
import com.final_project_leesanghun_team2.domain.dto.comment.request.CommentUpdateRequest;
import com.final_project_leesanghun_team2.domain.dto.comment.request.ReplySaveRequest;
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

    @Column(nullable = false)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Comment parent;

    // 생성 메서드

    // 댓글 생성
	public static Comment createComment(CommentSaveRequest request, Post post, User user) {
        return new Comment(request, post, user);
	}
    public Comment(CommentSaveRequest request, Post post, User user) {
        this.content = request.getContent();
        this.user = user;
        this.post = post;
    }

    // 답글 생성
    public static Comment createReply(ReplySaveRequest request, Post findPost, Comment findComment, User user) {
        return new Comment(request, findPost, findComment, user);
    }
    public Comment(ReplySaveRequest request, Post post, Comment findComment, User user) {
        this.content = request.getContent();
        this.user = user;
        this.post = post;
        this.parent = findComment;
    }

    // 수정
    public void update(CommentUpdateRequest request) {
        this.content = request.getContent();
    }
}

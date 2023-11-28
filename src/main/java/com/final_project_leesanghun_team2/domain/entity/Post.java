package com.final_project_leesanghun_team2.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.final_project_leesanghun_team2.domain.dto.post.PostSaveRequest;
import com.final_project_leesanghun_team2.domain.dto.post.PostUpdateRequest;
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

    private Long likeCount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @JsonIgnore
    @OneToMany(mappedBy = "post", cascade = CascadeType.REMOVE)
    private List<Comment> commentList = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "post", cascade = CascadeType.REMOVE)
    private List<Likes> likesList = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TagPost> tagPostList = new ArrayList<>();

    // 생성 메서드
    public static Post createPost(PostSaveRequest request, User user) {
        return new Post(request, user);
    }
    public Post(PostSaveRequest request, User user) {
        this.content = request.getContent();
        this.visibility = request.isVisibility();
        this.user = user;
        this.likeCount = 0L;
    }

    // 수정
    public void update(PostUpdateRequest request) {
        this.content = request.getContent();
        this.visibility = request.isVisibility();
    }

    // 좋아요 증가 메서드
    public void increaseLikeCount() {
        this.likeCount++;
    }

    // 좋아요 감소 메서드
    public void decreaseLikeCount() {
        if (likeCount - 1 < 0) this.likeCount = 0L;
        else this.likeCount--;
    }
}
